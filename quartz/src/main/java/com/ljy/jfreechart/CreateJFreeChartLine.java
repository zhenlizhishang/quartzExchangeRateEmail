package com.ljy.jfreechart;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.labels.StandardCategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import com.ljy.util.XMLData;

public class CreateJFreeChartLine {   
  
    
     
    /**  
     * 创建JFreeChart Line Chart（折线图）  
     */  
    public static void generateImg(List<Map<String, Object>> list, double minNum, double maxNum){
    	//步骤1：创建CategoryDataset对象（准备数据）   
        CategoryDataset dataset = createDataset(list);   
        //步骤2：根据Dataset 生成JFreeChart对象，以及做相应的设置   
        JFreeChart freeChart = createChart(dataset, minNum, maxNum);   
        //步骤3：将JFreeChart对象输出到文件，Servlet输出流等   
        saveAsFile(freeChart, XMLData.PICTUREURL, 800, 600); 
    }
  
    // 保存为文件   
    public static void saveAsFile(JFreeChart chart, String outputPath, int weight, int height) {   
        FileOutputStream out = null;   
        try {   
            File outFile = new File(outputPath);   
            if (!outFile.getParentFile().exists()) {   
                outFile.getParentFile().mkdirs();   
            }   
            out = new FileOutputStream(outputPath);   
            // 保存为PNG   
            ChartUtilities.writeChartAsPNG(out, chart, weight, height);   
            // 保存为JPEG   
            // ChartUtilities.writeChartAsJPEG(out, chart, weight, height);   
            out.flush();   
        } catch (FileNotFoundException e) {   
            e.printStackTrace();   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {   
            if (out != null) {   
                try {   
                    out.close();   
                } catch (IOException e) {   
                    // do nothing   
                }   
            }   
        }   
    }   
  
    // 根据CategoryDataset创建JFreeChart对象   
    @SuppressWarnings("deprecation")
	public static JFreeChart createChart(CategoryDataset categoryDataset, double minNum, double maxNum) {   
    	
    	//创建主题样式  
		StandardChartTheme standardChartTheme=new StandardChartTheme("CN");  
		//设置标题字体  
		standardChartTheme.setExtraLargeFont(new Font("隶书", Font.BOLD, 20));  
		//设置图例的字体  
		standardChartTheme.setRegularFont(new Font("宋书", Font.PLAIN, 15));  
		//设置轴向的字体  
		standardChartTheme.setLargeFont(new Font("宋书", Font.PLAIN, 15));  
		//应用主题样式  
		ChartFactory.setChartTheme(standardChartTheme); 
    	
        // 创建JFreeChart对象：ChartFactory.createLineChart   
        JFreeChart jfreechart = ChartFactory.createLineChart(
        		"美元汇率价格波动图", // 标题   
                "日期", // categoryAxisLabel （category轴，横轴，X轴标签）   
                "价格", // valueAxisLabel（value轴，纵轴，Y轴的标签）   
                categoryDataset, // dataset   
                PlotOrientation.VERTICAL, true, // legend   
                false, // tooltips   
                false); // URLs   
  
        // 使用CategoryPlot设置各种参数。以下设置可以省略。   
        CategoryPlot plot = (CategoryPlot) jfreechart.getPlot();   
        // 背景色 透明度   
        plot.setBackgroundAlpha(0.1f);   
        // 前景色 透明度   
        plot.setForegroundAlpha(0.8f);   
        // 其他设置 参考 CategoryPlot类   
  
        // 设置显示数据点
        LineAndShapeRenderer renderer = (LineAndShapeRenderer)plot.getRenderer();
        DecimalFormat decimalformat1 = new DecimalFormat("##.#####");//数据点显示数据值的格式
        renderer.setItemLabelGenerator(new StandardCategoryItemLabelGenerator("{2}", decimalformat1));
        //上面这句是设置数据项标签的生成器
        renderer.setItemLabelsVisible(true);//设置项标签显示
        renderer.setBaseItemLabelsVisible(true);//基本项标签显示
        //上面这几句就决定了数据点按照设定的格式显示数据值  
        renderer.setShapesFilled(Boolean.TRUE);//在数据点显示实心的小图标
        renderer.setShapesVisible(true);//设置显示小图标
        
     // 设置X轴
        CategoryAxis domainAxis = plot.getDomainAxis();   
        domainAxis.setLabelFont(new Font("宋书", Font.PLAIN, 15)); // 设置横轴字体
        domainAxis.setTickLabelFont(new Font("宋书", Font.PLAIN, 15));// 设置坐标轴标尺值字体
        domainAxis.setLowerMargin(0.01);// 左边距 边框距离
        domainAxis.setUpperMargin(0.06);// 右边距 边框距离,防止最后边的一个数据靠近了坐标轴。
        domainAxis.setMaximumCategoryLabelLines(10);
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.DOWN_45);// 横轴 lable 的位置 横轴上的 Lable 45度倾斜 DOWN_45

        // 设置Y轴
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setLabelFont(new Font("宋书", Font.PLAIN, 15)); 
        rangeAxis.setStandardTickUnits(NumberAxis.createIntegerTickUnits());//Y轴显示整数
//        rangeAxis.setAutoRangeMinimumSize(1);   //最小跨度
        rangeAxis.setUpperMargin(0.18);//上边距,防止最大的一个数据靠近了坐标轴。   
        rangeAxis.setLowerBound(minNum);   //最小值
        rangeAxis.setUpperBound(maxNum);  //最大值
//        rangeAxis.setAutoRange(false);   //不自动分配Y轴数据
        rangeAxis.setTickMarkStroke(new BasicStroke(1.6f));     // 设置坐标标记大小
        rangeAxis.setTickMarkPaint(Color.BLACK);     // 设置坐标标记颜色
        rangeAxis.setTickUnit(new NumberTickUnit(0.01));//每10个刻度显示一个刻度值
        
        return jfreechart;   
    }   
  
    /**  
     * 创建CategoryDataset对象  
     *   
     */  
    public static CategoryDataset createDataset(List<Map<String, Object>> list) {   
         DefaultCategoryDataset categoryDataset = new DefaultCategoryDataset();
         
         for (int i = list.size(); i >0; i--) {
        	 Map<String, Object> map = list.get(i-1);
        	 categoryDataset.addValue(Double.parseDouble(map.get("prices").toString()), map.get("name").toString(), map.get("time").toString()); 
		}
        return categoryDataset; 
        
     // 或者使用类似以下代码   
//      String[] rowKeys = { "One", "Two", "Three" };   
//      String[] colKeys = { "1987", "1997", "2007" };   
//
//      double[][] data = { { 50, 20, 30 }, { 20, 10D, 40D },   
//              { 40, 30.0008D, 38.24D }, };  
//        return DatasetUtilities.createCategoryDataset(rowKeys, colKeys, data);   
    }   
    
    public static void main(String[] args) {   
        CategoryDataset dataset = createDataset(null);   
        JFreeChart freeChart = createChart(dataset, 0, 0);   
        saveAsFile(freeChart, XMLData.PICTUREURL, 600, 400);   
    }  
}
