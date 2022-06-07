# 도담도담(Family Network Service)
### 활용 기술(FE)
- Android Native(Kotlin)
- SharedPreference, retrofit2, coroutine, hilt
- lottie, Glide, recyclerView

### 프로젝트 소개
- SNS가 어느정도 무르익은 2010년도 초반대부터 FNS에 대한 시도는 계속되어 왔다.
- 계속되는 실패 속에 우리는 그 원인이 Family Network에 대한 Social Network적인 접근에 있다고 생각했다.
- 가족이라는 관계는 논리적 관계인 일반적 사회관계와 다르다. 한마디로 감성적이면서 의도와 목적이 없는 관계다.
- 우리는 이 특징에 집중하여 Nudge라는 개념(팔꿈치로 슬쩍 찌르다.)을 벤치마킹한 소통을 유도하는 부드러운 개입이라는 컨셉으로 FNS를 만들어 내고자 하였다.

### 내가 맡은 역할(FE)
- 로그인 이후 가족 및 프로필 생성 기능 구현
  - UI 제작
  - 가족코드 입력에 따른 API요청 분기처리
- 오늘의 상태입력 구현
  - UI 제작
  - recyclerView를 활용한 이모티콘 선택창 구현
- 프로필 이미지 & 가족사진 설정 구현
  - UI 제작
  - MultipartFormData를 이용한 이미지 소스 및 데이터 송수신
  - 가족사진은 스마트폰 갤러리에서 사진을 선택하도록 구현
  - 프로필 이미지는 갤러리와 일러스트 중 선택하여 설정 가능하도록 구현
- 소원나무 구현
  - UI 제작
  - 8개의 선물상자를 하드코딩하여 구현
  - 소원 등록 시 랜덤하게 숫자를 부여하여(중복x) 소원상자가 보이도록 설정
  - lottie 에니메이션을 활용하여 선물상자 조회 시 상자가 열리는 효과 구현

### 어려웠던 점
- 이번 프로젝트는 프론트 3명 중 나만 안드로이드 개발이 처음이었다. 그래도 다행히 교보재로 패스트 캠퍼스 강의를 받아서 학습을 수월하게 할 수 있었는데 문제는 시간이었다.
- 이번 프로젝트는 6주라는 시간밖에 주어지지 않았고 첫주차에는 기획에 모든 시간을 쏟았다. 교보재도 2주차 끝날 즈음에 나와서 그 주말부터 평일/주말 가릴 것 없이 계속 공부했다.
- 나머지 팀원들이 아키텍쳐적인 고민을 하는 동안 나는 코틀린과 안드로이드 개발을 공부했다. 팀원들에게 미안했지만 미안한 마음만으로는 아무것도 해결되지 않는다는 생각으로 전력을 다해 공부했고 일주일 조금 넘는 시간동안 개발에 참여할 수 있을정도의 실력을 만들어 냈다.
- 덕분에 짧은 시간이지만 개발에 기여할 수 있었고 위에서 명시한 기능들을 모두 구현하는데 성공했다.
- 팀원들이 짜놓은 아키텍쳐 위에서 작업을 할 수 있어서 편하게 학습하고 개발할 수 있었지만 그 고민의 과정에 참여하지 못해서 처음에 이해하기가 조금 어려웠었고 어떻게든 도움이 되기 위해 노력하는 과정에서 멘탈관리가 가장 힘들었던 프로젝트인 것 같다.
- 결과적으로 이 프로젝트는 최종 발표에서 1등에 선정되어 본선발표까지 진출했으니 지금까지 프로젝트 중 가장 만족하고 애정하는 프로젝트이다.

### 배운 점
#### 안드로이드 개발 경험
: 앱 개발에 대한 지식이 전혀 없는 상태에서 무작정 안드로이드 개발에 도전했다. Kotlin기본 문법부터 UI 제작, 기능구현까지 기본적인 틀은 웹과 비슷했지만 기능구현에 있어서는 Java를 배워본 적이 없이 시작해서 어려움이 있었다. Kotlin기본 문법부터 정말 열심히 공부했고 구글과 함께라면 보통 수준의 기능 구현은 가능하기에 이르렀다. 객체지향적인 개념을 확실히 공부할 수 있었고 변수마다 자료형을 명시하는 부분도 지금껏 해본적 없었기에(Python, JS) 새로운 경험이었다. 앞으로 Typescript를 학습하는데에도 큰 도움이 될 것 같다.
#### lottie
: 애니메이션은 구현하기 어렵지만 상당히 괜찮은 사용자 경험을 제공한다. lottie는 고퀄리티의 애니메이션 기능을 무료로 사용하기 쉽게 제공해준다는 사실에 놀랐고 앱 뿐만 아니라 웹에서도 사용이 가능하다는 것을 이번 프로젝트를 하면서 알게 되었다. 이번 프로젝트에서는 소원나무 기능을 구현할 때 사용하였는데 앞으로도 유용하게 사용할 것 같은 활용도 높은 라이브러리다. 애니메이션 시작시점, 중간, 끝나는 시점 각각에 코드를 작성할 수도 있다.(addAnimatorListener 활용)
#### 디자인 패턴
- Model : 앱에서 사용되는 데이터와 그 데이터를 처리하는 부분
- View : 사용자에게 보여지는 UI 부분
1. MVC
- Controller : 사용자의 입력을 받고 처리하는 부분
- 동작 순서  
  a. 사용자의 Action들이 Controller에 들어옴  
  b. Controller에서 해당하는 Model 업데이트  
  c. Controller에서 해당하는 View 선택  
  d-1. View에서 업데이트된 Model을 이용하여 화면 업데이트  
  d-2. Model에서 View에게 Notify하여 업데이트  
  d-3. View에서 Polling으로 주기적으로 Model의 변경을 감지하여 업데이트  
- 특징  
  a. 하나의 Controller가 여러개의 View를 선택 할 수 있음(1:n)  
  b. Controller는 View를 업데이트하지 않음  
  c. 단순해서 가장 많이 사용됨  
  d. View와 Model의 의존성이 높아서 규모가 커질수록 유지보수가 어려워짐  

2. MVP
- Presenter : View에서 요청한 정보로 Model을 가공하여 View에 전달해주는 부분
- 동작 순서  
  a. 사용자의 Action들은 View를 통해 들어옴  
  b. View는 데이터를 Presenter에게 요청  
  c. Presenter는 Model에게 데이터를 요청  
  d. Model은 Presenter에게 요청받은 데이터를 응답  
  e. Presenter는 View에게 데이터를 응답  
  f. View는 Presenter가 응답한 데이터를 이용하여 화면 구현  
- 특징  
  a. Presenter와 View는 1:1 관계  
  b. View와 Model의 의존성을 없앰  
  c. View와 Presenter 간에 의존성이 생김  
  
3. MVVM
- View Model : View를 표현하기 위해 만든 View를 위한 Model. 데이터 처리까지 수행
- 동작 순서  
  a. 사용자의 Action들은 View를 통해 들어옴  
  b. View에 Action이 들어오면, **Command 패턴**으로 View Model에 Action을 전달  
  c. View Model은 Model에게 데이터를 요청  
  d. Model은 데이터를 응답  
  e. View Model은 응답받은 데이터를 가공하여 저장  
  f. View는 View Model의 **데이터를 바인딩**하여 화면을 구성  
- 특징  
  a. View와 View Model은 1:n 관계  
  b. Model, View, View Model 간에 의존성을 모두 없앰  
  c. View Model의 설계가 쉽지 않음  
- Command 패턴
  - Command : Receiver의 정보 + 행동이 들어있는 객체
  - Invoker : Command를 저장하는 객체
  - Receiver : 행동을 담당하는 객체(기능을 수행)
  - 나는 커맨드 패턴을 게임에 비유해서 이해했다. 키보드 각 버튼마다 스킬을 등록할 수 있는데 이를 등록하는 객체가 Invoker(발동자), 이용자의 Action을 인지해서 Command를 발동시킨다. Command는 각 스킬의 명칭과 결과값을 보여주는 일종의 인터페이스다. 마지막으로 Receiver는 해당하는 커맨드가 실행되었을 때 데미지를 입히거나 회복을 하는 등 실제 동작을 정의하는 객체이다. 결과적으로 유저 입장에서는 스킬을 특정 버튼에 등록하고 그 버튼을 누르면 스킬이 나가는 결과를 보게 된다.

##### 디자인패턴은 글로써는 완벽하게 이해하기 어려운 것 같다. 다만 앞으로 경험을 쌓아 나가면서 함께 공부하고 실제로 적용시키다보면 실력이 더 빨리 늘 수 있는 좋은 방법인 것 같다.



