package com.ssafy.family.ui.main.bottomFragment

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kizitonwose.calendarview.model.CalendarDay
import com.kizitonwose.calendarview.model.CalendarMonth
import com.kizitonwose.calendarview.model.DayOwner
import com.kizitonwose.calendarview.ui.DayBinder
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder
import com.kizitonwose.calendarview.ui.ViewContainer
import com.ssafy.family.R
import com.ssafy.family.data.remote.res.ScheduleInfo
import com.ssafy.family.databinding.CalendarDayBinding
import com.ssafy.family.databinding.CalendarHeaderBinding
import com.ssafy.family.databinding.FragmentCalendarBinding
import com.ssafy.family.ui.Adapter.ScheduleAdapter
import com.ssafy.family.ui.home.LoginViewModel
import com.ssafy.family.ui.schedule.ScheduleActivity
import com.ssafy.family.util.CalendarUtil.dayLocalDateToString
import com.ssafy.family.util.CalendarUtil.daysOfWeekFromLocale
import com.ssafy.family.util.CalendarUtil.makeInVisible
import com.ssafy.family.util.CalendarUtil.makeVisible
import com.ssafy.family.util.CalendarUtil.monthLocalDateToString
import com.ssafy.family.util.CalendarUtil.setTextColorRes
import com.ssafy.family.util.CalendarUtil.stringToLocalDate
import com.ssafy.family.util.LoginUtil
import com.ssafy.family.util.Status
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.DateTimeFormatter

@AndroidEntryPoint
@RequiresApi(Build.VERSION_CODES.O)
class CalendarFragment : Fragment() {

    private lateinit var binding: FragmentCalendarBinding
    private val calendarViewModel by activityViewModels<CalendarViewModel>()
    private val loginViewModel by activityViewModels<LoginViewModel>()

    private var mContext: Context? = null
    private var selectedDate: LocalDate? = null
    private val selectionFormatter = DateTimeFormatter.ofPattern("MMM d???")
    private var scheduleMonthList = mutableMapOf<LocalDate, MutableList<ScheduleInfo>>()
    private var scheduleDayList = mutableListOf<ScheduleInfo>()
    private var eventsAdapter: ScheduleAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        //??? ?????? ?????? ??????
        scheduleMonthList = mutableMapOf()
        calendarViewModel.getMonthSchedule(monthLocalDateToString(calendarViewModel.today))
        initCalendar()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //??? ?????? ?????? ???????????? ?????????
        calendarViewModel.getMonthRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(!it.data!!.schedules.isNullOrEmpty()){
                        for (a in it.data.schedules){
                            var idx = 0L
                            while(stringToLocalDate(a.startDate).plusDays(idx) != stringToLocalDate(a.endDate).plusDays(1)){
                                if(scheduleMonthList[stringToLocalDate(a.startDate).plusDays(idx)] !=null){
                                    val list = scheduleMonthList[stringToLocalDate(a.startDate).plusDays(idx)]!!
                                    list.add(a)
                                    scheduleMonthList[stringToLocalDate(a.startDate).plusDays(idx)] = list
                                }else{
                                    scheduleMonthList[stringToLocalDate(a.startDate).plusDays(idx)] = mutableListOf(a)
                                }
                                idx++
                            }
                        }
                    }
                    dismissMonthLoading()
                    initCalendar()
                    selectDate(calendarViewModel.today)
                    updateAdapterForDate()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "???????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show()
                    dismissMonthLoading()
                }
                Status.LOADING -> {
                    setMonthLoading()
                }
                Status.EXPIRED -> {
                    dismissMonthLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "?????? ??????????????????", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //???????????????????????? ?????? ????????? ?????? ??? ?????? ????????? ??????
        eventsAdapter = ScheduleAdapter {
            val intent = Intent(requireContext(),ScheduleActivity::class.java)
            intent.putExtra("sID", it.scheduleId)
            startActivity(intent)
        }

        // ??? ?????? ?????? ?????? ?????????
        calendarViewModel.getDayRequestLiveData.observe(requireActivity()){
            when (it.status) {
                Status.SUCCESS -> {
                    if(!it.data!!.schedules.isNullOrEmpty()){
                        scheduleDayList = it.data.schedules.toMutableList()
                        updateAdapterForDate()
                    }else{
                        scheduleDayList = mutableListOf()
                        updateAdapterForDate()
                    }
                    dismissDayLoading()
                }
                Status.ERROR -> {
                    Toast.makeText(requireActivity(), "????????? ???????????? ????????????.", Toast.LENGTH_SHORT).show()
                    dismissDayLoading()
                }
                Status.LOADING -> {
                    setDayLoading()
                }
                Status.EXPIRED -> {
                    dismissDayLoading()
                    loginViewModel.MakeRefresh(LoginUtil.getUserInfo()!!.refreshToken)
                    Toast.makeText(requireActivity(), "?????? ??????????????????", Toast.LENGTH_SHORT).show()
                }
            }
        }

        //????????????????????? ??? ?????? ?????? ????????? ??????
        binding.recyclerSchedule.apply {
            layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            adapter = eventsAdapter
        }

        // ?????? ?????? ??????
        binding.refreshButton.setOnClickListener {
            scheduleMonthList = mutableMapOf()
            calendarViewModel.getMonthSchedule(monthLocalDateToString(selectedDate!!))
        }

        //?????? ?????? ??????
        binding.addButton.setOnClickListener {
            val intent = Intent(requireContext(),ScheduleActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initCalendar() {

        //?????? ?????? ??????
        val daysOfWeek = daysOfWeekFromLocale()
        var currentMonth = YearMonth.now()
        binding.calendar.apply {
            setup(currentMonth.minusMonths(100), currentMonth.plusMonths(100), daysOfWeek.first())
            scrollToMonth(currentMonth)
        }

        //?????? ??????(??????, ??????, ?????? ?????? ???)
        binding.calendar.dayBinder = object : DayBinder<DayViewContainer> {
            override fun create(view: View) = DayViewContainer(view)
            override fun bind(container: DayViewContainer, day: CalendarDay) {
                container.day = day
                val textView = container.binding.dayText
                val dotView = container.binding.dotView

                textView.text = day.date.dayOfMonth.toString()

                if (day.owner == DayOwner.THIS_MONTH) {
                    textView.makeVisible()
                    when (day.date) {
                        LocalDate.now() -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.circle_schedule)
                            dotView.makeInVisible()
                        }
                        selectedDate -> {
                            textView.setTextColorRes(R.color.white)
                            textView.setBackgroundResource(R.drawable.circle_selsected_schedule)
                            dotView.makeInVisible()
                        }
                        else -> {
                            textView.setTextColorRes(R.color.black)
                            textView.background = null
                            dotView.isVisible = scheduleMonthList[day.date].orEmpty().isNotEmpty()
                        }
                    }
                } else {
                    textView.makeInVisible()
                    dotView.makeInVisible()
                }
            }
        }

        //?????? ????????? ?????????
        binding.calendar.monthScrollListener = {
            if(it.yearMonth.month==calendarViewModel.today.month){
                selectDate(calendarViewModel.today)
                updateAdapterForDate()
            }else{
                selectDate(it.yearMonth.atDay(1))
            }
        }

        //?????? ?????? ???, ?????? ??? ?????? ??????
        binding.claendarAfterButton.setOnClickListener {
            currentMonth = currentMonth.plusMonths(1)
            binding.calendar.scrollToMonth(currentMonth)
        }

        binding.claendarBeforeButton.setOnClickListener {
            currentMonth = currentMonth.minusMonths(1)
            binding.calendar.scrollToMonth(currentMonth)
        }

        //?????? ?????? ??????
        class MonthViewContainer(view: View) : ViewContainer(view) {
            val legendLayout = CalendarHeaderBinding.bind(view).legendLayout
        }

        binding.calendar.monthHeaderBinder = object :
            MonthHeaderFooterBinder<MonthViewContainer> {
            override fun create(view: View) = MonthViewContainer(view)
            override fun bind(container: MonthViewContainer, month: CalendarMonth) {}
        }
    }

    //?????? ???????????? ?????????????????? ???????????? ?????????
    inner class DayViewContainer(view: View) : ViewContainer(view) {
        lateinit var day: CalendarDay // Will be set when this container is bound.
        val binding = CalendarDayBinding.bind(view)

        init {
            view.setOnClickListener {
                if (day.owner == DayOwner.THIS_MONTH) {
                    calendarViewModel.today = day.date
                    selectDate(calendarViewModel.today)
                }
            }
        }
    }

    //????????? ?????? : ?????? ????????? ??????????????? ????????????.
    fun selectDate(date: LocalDate) {
        calendarViewModel.getDaySchedule(dayLocalDateToString(date))
        val oldDate = selectedDate
        selectedDate = date
        oldDate?.let { binding.calendar.notifyDateChanged(it) }
        binding.calendar.notifyDateChanged(date)
        binding.selectedMonthText.text = "${date.year}??? ${date.monthValue}??? "
        binding.selectedDateText.text = selectionFormatter.format(date)
    }

    //????????? ????????? ????????????????????? ?????? ???????????? ?????????
    private fun updateAdapterForDate() {
        eventsAdapter?.apply {
            scheduleList.clear()
            scheduleList.addAll(scheduleDayList)
            notifyDataSetChanged()
        }
    }

    //??? ?????? ?????? ?????????
    private fun setMonthLoading() {
        binding.progressBarMonthLoading.visibility = View.VISIBLE
    }

    private fun dismissMonthLoading() {
        binding.progressBarMonthLoading.visibility = View.GONE
    }

    //??? ?????? ?????? ?????????
    private fun setDayLoading() {
        binding.progressBarDayLoading.visibility = View.VISIBLE
    }

    private fun dismissDayLoading() {
        binding.progressBarDayLoading.visibility = View.GONE
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

}
