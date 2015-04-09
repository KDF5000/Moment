package com.ktl.moment.android.component.wheel;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.util.Log;
import android.util.Xml;

public class AreaDbOption {
	
	private List<Area> getProvinces(Context context,String xmlPath){
		Area area = null;
		ArrayList<Area> areas = new ArrayList<Area>();
	        InputStream inputStream=null;      
	        //获得XmlPullParser解析器  
	        XmlPullParser xmlParser = Xml.newPullParser();     
	        try {  
	            //得到文件流，并设置编码方式  
	            inputStream=context.getResources().getAssets().open(xmlPath);  
	            xmlParser.setInput(inputStream, "utf-8");  
	            //获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。  
	            int evtType=xmlParser.getEventType();  
	         //一直循环，直到文档结束      
	         while(evtType!=XmlPullParser.END_DOCUMENT){   
	            switch(evtType){   
	            case XmlPullParser.START_TAG:  
	                String tag = xmlParser.getName();   
	                
	                //如果是river标签开始，则说明需要实例化对象了  
	                if (tag.equalsIgnoreCase("Province")) {
	                	area = new Area();
	                	int id = Integer.parseInt(xmlParser.getAttributeValue(0));
	                	String provinceName = xmlParser.getAttributeValue(1);
	                	area.setId(id);
	                	area.setProvince(provinceName);
	                }  
	                break;  
	                  
	           case XmlPullParser.END_TAG:  
	             //如果遇到river标签结束，则把river对象添加进集合中  
	               if (xmlParser.getName().equalsIgnoreCase("Province") && area != null) {   
	            	   areas.add(area); 
	            	   area = null;   
	               }  
	                break;   
	                default:break;  
	            }  
	            //如果xml没有结束，则导航到下一个river节点  
	            evtType=xmlParser.next();  
	         }  
	        } catch (XmlPullParserException e) {  
	            // TODO Auto-generated catch block  
	            e.printStackTrace();  
	        }catch (IOException e1) {  
	            // TODO Auto-generated catch block  
	            e1.printStackTrace();  
	        }   
	        
Log.i("MMM", areas+"省集合");
	        return areas;   
	    
	}
	
	public List<Area> getAreas(Context context,String xmlPath){
		String cityXmlPath = "Cities.xml";
		int lastPid = 1;
		ArrayList<Area> areas = (ArrayList<Area>) getProvinces(context, xmlPath);
		InputStream inputStream=null;      
        //获得XmlPullParser解析器  
        XmlPullParser xmlParser = Xml.newPullParser();     
        try {  
            //得到文件流，并设置编码方式  
            inputStream=context.getResources().getAssets().open(cityXmlPath);  
            xmlParser.setInput(inputStream, "utf-8");  
            //获得解析到的事件类别，这里有开始文档，结束文档，开始标签，结束标签，文本等等事件。  
            int evtType=xmlParser.getEventType();  
            Area area = null;
         //一直循环，直到文档结束   
   
         ArrayList<String> citys = new ArrayList<String>();
         while(evtType!=XmlPullParser.END_DOCUMENT){   
			switch(evtType){   
            case XmlPullParser.START_TAG:  
                String tag = xmlParser.getName();   
                //如果是river标签开始，则说明需要实例化对象了  
                if (tag.equalsIgnoreCase("City")) {
                	int tempPid = Integer.parseInt(xmlParser.getAttributeValue(2));
                	if(tempPid != lastPid){
                		citys = new ArrayList<String>();
                		lastPid = tempPid;
                	}
                	String cityName = xmlParser.getAttributeValue(1);
                	
                	area = areas.get(lastPid-1);
                	citys.add(cityName);
                	area.setCitys(citys);
                }  
                break;  
                  
           case XmlPullParser.END_TAG:  
               if (xmlParser.getName().equalsIgnoreCase("City") && area != null) {   
               }  
                break;   
                default:break;  
            }  
            //如果xml没有结束，则导航到下一个river节点  
            evtType=xmlParser.next();  
         } 
        } catch (XmlPullParserException e) {  
            e.printStackTrace();  
        }catch (IOException e1) {  
            e1.printStackTrace();  
        }   
        return areas; 
	}
}
