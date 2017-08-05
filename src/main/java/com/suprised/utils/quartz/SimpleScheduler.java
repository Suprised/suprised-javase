package com.suprised.utils.quartz;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;

import org.quartz.DateBuilder.IntervalUnit;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.triggers.CalendarIntervalTriggerImpl;

import com.suprised.utils.Threads;

public class SimpleScheduler {

	public void run() throws SchedulerException {

		// 通过SchedulerFactory获取一个调度器实例
		// DirectSchedulerFactory.getInstance().cre
		SchedulerFactory sf = new StdSchedulerFactory();

		Scheduler sched = sf.getScheduler();
		Date runTime = evenMinuteDate(new Date());

		// 通过过JobDetail封装HelloJob，同时指定Job在Scheduler中所属组及名称，这里，组名为group1，而名称为ScanDirectoryJob。
		JobDetail job = newJob(ScanDirectoryJob.class).withIdentity("ScanDirectoryJob", "group1").build();
		job.getJobDataMap().put("SCAN_DIR", "E:\\ljx\\sql"); // 参数
		job.getJobDataMap().put("extends", ".sql");
		
		// 创建一个SimpleTrigger实例，指定该Trigger在Scheduler中所属组及名称。
		// 接着设置调度的时间规则.当前时间运行
		Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
		
		// 5秒执行一次
		trigger = new CalendarIntervalTriggerImpl("trigger1", "group1", IntervalUnit.SECOND, 5);
		
		// 注册并进行调度
		sched.scheduleJob(job, trigger);

		// 启动调度器
		// sched.start();
		Threads.sleep(65L * 1000L); // 程序暂停65S
		// 调度器停止运行
		// sched.shutdown(true);
		System.out.println("结束运行。。。。");
	}
	
	public static void main(String[] args) throws SchedulerException {
		new SimpleScheduler().run();
	}
}
