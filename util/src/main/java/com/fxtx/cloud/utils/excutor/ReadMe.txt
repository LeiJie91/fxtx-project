/**
 * 线程池调用demo
 *
*/
// 调用进程池时，请参照ExcuteTaskRun.java实现Runnable接口，在其中的run()方法中填写相应的业务逻辑
// 实现类的名字，请保持*TaskRun.java规范
// 注:尽量不要使用callTask.java实例
// 调用:
ExecutorProcessPool.getInstance().executeRunableTask(new ExcuteTaskRun("1"));