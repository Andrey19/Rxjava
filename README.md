#RxKava
#Задачи 
1) Самая популярная, на конфликт subscribeOn и observeOn приходится 80% задач RX. Какой результат будет в логе?
   //onSubscribeThread = main
   //mapThread = RxNewThreadScheduler-1
   //flatMapThread = RxSingleScheduler-1
   //subscribeThread = RxCachedThreadScheduler-1, value = 0
2) Какой результат будет в логе? Как переписать, чтоб все вывести (2 варианта есть)
  1. val subject = PublishSubject.create<String>()
   subject.subscribe { println(it) }
   subject.onNext( "1")
   subject.onNext( "2")
   subject.onNext( "3")
  2. заменить ReplaySubject

    