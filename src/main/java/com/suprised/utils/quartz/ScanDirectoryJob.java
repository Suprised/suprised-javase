package com.suprised.utils.quartz;

import java.io.File;
import java.io.FileFilter;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时任务Job
 */
public class ScanDirectoryJob implements Job {

	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		JobDetail jobDetail = context.getJobDetail();
		String jobName = jobDetail.getKey().getName();
		System.out.println(jobName + " fired at " + new Date());// 记录任务开始执行的时间
		JobDataMap dataMap = jobDetail.getJobDataMap();// 任务所配置的数据映射表
		String dirName = dataMap.getString("SCAN_DIR");// 获取要扫描的目录
		final String ext = dataMap.getString("extends"); //操作的文件后缀
		if (dirName == null) {// 所需要的扫描目录没有提供
			throw new JobExecutionException("Directory not configured");
		}
		// Make sure the directory exists
		File dir = new File(dirName);
		if (!dir.exists()) {// 提供的是错误目录
			throw new JobExecutionException("Invalid Dir " + dirName);
		}
		// 只统计xml文件
		File[] files = dir.listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				String lCaseFilename = pathname.getName().toLowerCase();// 小写化
				return (pathname.isFile() && (lCaseFilename.indexOf(ext) > 0)) ? true : false;
			}
		});
		if (files == null || files.length <= 0) {
			System.out.println("No XML files found in " + dir);
			return;
		}
		int size = files.length;
		for (int i = 0; i < size; i++) {
			File file = files[i];
			File aFile = file.getAbsoluteFile();
			long fileSize = file.length();
			String msg = aFile + " - Size: " + fileSize;
			System.out.println(msg);
		}
	}

}
