/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.eii.jeeclassproject.jeeblog.controller;

import com.eii.jeeclassproject.jeeblog.dao.PostDao;
import com.eii.jeeclassproject.jeeblog.weblistner.AuthenticatedCounter;
import com.eii.jeeclassproject.jeeblog.weblistner.SessionCounter;
import com.sun.management.OperatingSystemMXBean;
import java.io.Serializable;
import java.lang.management.ManagementFactory;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.DateAxis;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author winiga
 */
@Named("admin")
@ViewScoped
public class AdminController implements Serializable {

    @EJB
    private PostDao postDao;

    private static HashMap<Date, String> incidents = new HashMap<>();

    public String getAllCurentSession() {

        return String.valueOf(SessionCounter.getCount());
    }

    public String getAuthSessionCount() {

        return String.valueOf(AuthenticatedCounter.getAuthCount());
    }

    public String getAllTotalView() {

        return String.valueOf(postDao.getAllTotalViewCount());
    }

    public void noteIncidents(String error) {
        System.out.println(error);
        incidents.put(new Date(System.currentTimeMillis()), error);
    }

    public void resetIncidents() {
        incidents.clear();
    }

    public String getIncidentsCount() {
        return String.valueOf(incidents.size());
    }

    public LineChartModel getCreatePostStatsModel() {

        LineChartModel postCreationStat = new LineChartModel();

        LineChartSeries series = new LineChartSeries();
        series.setLabel("Nombre d'articles");

        HashMap<Date, Long> postsStats = postDao.getCreatedByDate();

        for (Map.Entry<Date, Long> postStat : postsStats.entrySet()) {
            series.set(postStat.getKey(), postStat.getValue());
        }

        postCreationStat.addSeries(series);

        //postCreationStat.setTitle("Statistique de créations d'articles");
        postCreationStat.setZoom(true);
        postCreationStat.getAxis(AxisType.Y).setLabel("Articles");

        DateAxis axis = new DateAxis("Dates");
        axis.setTickAngle(-50);
        axis.setTickFormat("%b %y");

        postCreationStat.getAxes().put(AxisType.X, axis);

        return postCreationStat;
    }

//    public void cpuUsage() {
//        OperatingSystemMXBean osBean = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getPlatformMXBean(
//                OperatingSystemMXBean.class);
//// What % CPU load this current JVM is taking, from 0.0-1.0
//        System.out.println(osBean.getProcessCpuLoad());
//
//// What % load the overall system is at, from 0.0-1.0
//        System.out.println(osBean.getSystemCpuLoad());
//    }
}
