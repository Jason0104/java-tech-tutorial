package com.java.tech.mojo;



import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * created by Jason on 2020/2/27
 */
@Mojo(name = "autoScanCount", requiresProject = false, defaultPhase = LifecyclePhase.PACKAGE)
public class AutoScanCode extends AbstractMojo {

    //存放扫描的文件
    private static List<String> fileList = new ArrayList<>();

    @Parameter(name = "currentBaseDir", defaultValue = "/workspace")
    private String currentBaseDir;

    @Parameter(name = "suffix", defaultValue = ".java")
    private String suffix;

    //代码行数
    private int lines = 0;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException {
        autoScanDir(currentBaseDir);
        System.out.println("自动扫描代码插件总共扫描文件个数:" + fileList.size());
        System.out.println("自动扫描代码插件总共扫描代码行数:" + lines);
    }

    //扫描文件目录
    private void autoScanDir(String baseDir) {
        File files = new File(baseDir);

        for (File file : files.listFiles()) {
            //如果是目录 需要进行递归调用
            if (file.isDirectory()) {
                autoScanDir(file.getAbsolutePath());
            } else {
                if (file.getName().endsWith(suffix)) {
                    fileList.add(file.getName());
                    lines += countLine(file);
                }
            }
        }
    }

    //统计文件行数
    private int countLine(File file) {
        int codeLine = 0;

        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while (reader.ready()) {
                reader.readLine();
                codeLine++;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return codeLine;
    }
}
