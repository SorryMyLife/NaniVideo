package NaniVideo;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class Video
{
	private File pass_file = new File("C:\\Windows\\System32\\pass_j.dat");
	public void down(String url_name , String parm , String save_path ) //下载函数，本来是打算用作发送post请求来着。。。。
	{
		try {
			DataOutputStream dos = new DataOutputStream(checkCon(url_name).getOutputStream());
			dos.writeBytes(parm);
			dos.flush();
			dos.close();
			File file = new File(save_path);
			if(!file.exists())
			{
				file.mkdirs();
			}
			String save_name = url_name.substring(url_name.lastIndexOf("/")+1);
			System.out.println("文件将保存在 : " + save_path + "\\" + save_name);
			FileOutputStream fos = new FileOutputStream(new File(save_path +  save_name));
			InputStream input = checkCon(url_name).getInputStream();
			byte[] buff = new byte[255];
			int len = -1;
			while((len = input.read(buff)) != -1)
			{
				fos.write(buff, 0, len);
			}
			fos.close();
			input.close();
			System.out.println(save_name + " 下载完成!");
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private HttpURLConnection checkCon(String url_name) throws Exception //偷懒，自动配置链接
	{
			URL url = new URL(url_name);
			HttpURLConnection url_con = (HttpURLConnection) url.openConnection();
			url_con.setRequestMethod("GET");
			url_con.setRequestProperty("Content-Type", "text/plain");
			url_con.setRequestProperty("Charset", "UTF-8");
			url_con.setRequestProperty("Connection", "keep-alive");
			url_con.setRequestProperty("Cookie", "kg_mid=f1258f0976aacd00c41230cc38c3a96b; Hm_lvt_aedee6983d4cfc62f509129360d6bb3d=1531743036; Hm_lpvt_aedee6983d4cfc62f509129360d6bb3d=1531744752");
			url_con.setDoOutput(true);
			url_con.setDoInput(true);
			url_con.setUseCaches(false);
			url_con.setRequestProperty("User-Agent", "Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Mobile Safari/537.36");
			url_con.connect();
		return url_con;
	}
	public String printURL(String url_name) //用来打印原始网页
	{
		String str = "";
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(checkCon(url_name).getInputStream(),"UTF-8"));
			String line = "";
			while((line = br.readLine()) != null)
			{
				str += line + "\n";
			}
			br.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	public void getVideoId(String str , ArrayList<String> list) //获取视频id
	{
		
		String  id = "", reg = "thread_id\":\"\\d*\",\"post";
		Pattern p1 = Pattern.compile(reg);
		Matcher m1 = p1.matcher(str);
		while(m1.find())
		{
			id = m1.group(0).replaceAll("thread_id\":\"", "");
			id = id.replaceAll("\",\"post", "");
			list.add(id);
		}
		
//		return tmp;
	}
	
	public String getVideoId(String str)
	{
		
		String  tmp = "" ,id = "", reg = "thread_id\":\"\\d*\",\"post";
		Pattern p1 = Pattern.compile(reg);
		Matcher m1 = p1.matcher(str);
		while(m1.find())
		{
			id = m1.group(0).replaceAll("thread_id\":\"", "");
			id = id.replaceAll("\",\"post", "");
//			list.add(id);
			tmp += id + "\n";
		}
		
		return tmp;
	}
	
	public String getVideoUrl(String str) //获取视频链接
	{
		String reg = "video_url\":\"(.+?\\.mp4)" , tmp = "" , v_url = "";
		Pattern p1 = Pattern.compile(reg);
		Matcher m1 = p1.matcher(str);
		while(m1.find())
		{
			tmp = m1.group(0).replaceAll("video_url\":\"", "");
			v_url += tmp.replaceAll("\\\\", "") + "\n";
		}
		
		return v_url;
	}
	
	public void getVideoUrl(String str ,  ArrayList<String> list)
	{
		String reg = "video_url\":\"(.+?\\.mp4)" , tmp = "" , v_url = "";
		Pattern p1 = Pattern.compile(reg);
		Matcher m1 = p1.matcher(str);
		while(m1.find())
		{
			tmp = m1.group(0).replaceAll("video_url\":\"", "");
			v_url = tmp.replaceAll("\\\\", "");
			list.add(v_url);
		}
		
//		return v_url;
	}

	private String getPass()
	{
		String ps = "" , line = "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(pass_file));
			while((line = br.readLine()) != null)
			{
				ps += line;
			}
			br.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ps;
	}
	
	public void start(String save_path) //开始函数，提供最基本的使用
	{
		String parm = "Content-Encoding=gzip";
		String tmp_id = "" , id = "";
		String url_name = "https://nani.baidu.com/mo/q/nani/getHotVideo?client_type=2&thread_id=5518095607";
		String tmp_url = "https://nani.baidu.com/mo/q/nani/getHotVideo?client_type=2&thread_id="; //临时的链接，后面会需要替换
		String NewUrl = "";
		ArrayList<String> id_list = new ArrayList<>(); //定义两个集合，用来存取相对应的数据
		ArrayList<String> url_list = new ArrayList<>();
		int  count = 0 ,  flag_count = 0 ,file_count = 0; //定义计数
		boolean flag = true;
		try
		{
			while(flag)
			{
				System.out.println("开始第 " + flag_count+1 + " 次下载");
				String str = printURL(url_name);
				getVideoId(str, id_list);
				int id_len = id_list.size();
				System.out.println("一共获取了 " + id_len + " 个id");
				while(count <= id_len)
				{
					for(int i = 0;i<id_len;i++)
					{
						id = id_list.get(i);
						NewUrl = tmp_url+id;
//						System.out.println("获取视频链接停1秒");
//						new Thread().sleep(1000);
						getVideoUrl(printURL(NewUrl), url_list);
						count++;
					}
					//只要获取到了视频链,就开始进行遍历下载,然后归零,等待下一次的遍历下载
					if(url_list.size() >= 1)
					{
						System.out.println("一共获取了 " + url_list.size() + " 个视频链接");
						for(int j = 0;j<url_list.size();j++)
						{
							System.out.println("下载停1秒");
							new Thread().sleep(1000);
							down(url_list.get(j), parm, save_path);
							file_count++;
						}
						url_list.clear();
						System.out.println("已经下载完 " + file_count + " 个视频文件");
					}
				}
				tmp_id = id_list.get(id_len); //将上一次集合存储的最后一个视频id保存
				id_list.clear();
				count = 0;
				url_name = "";
				url_name = tmp_url + tmp_id; //组成新的链接
				flag_count++;
				if(flag_count == 99999999)
				{
					flag=false;
				}
			}
			System.out.println("该网站多半已经被你爬完了!");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void start_lock(String save_path) //开始函数，提供最基本的使用
	{
		Scanner s = new Scanner(System.in);
		String parm = "Content-Encoding=gzip";
		String tmp_id = "" , id = "";
		String url_name = "https://nani.baidu.com/mo/q/nani/getHotVideo?client_type=2&thread_id=5518095607";
		String tmp_url = "https://nani.baidu.com/mo/q/nani/getHotVideo?client_type=2&thread_id="; //临时的链接，后面会需要替换
		String NewUrl = "";
		ArrayList<String> id_list = new ArrayList<>(); //定义两个集合，用来存取相对应的数据
		ArrayList<String> url_list = new ArrayList<>();
		int  count = 0 ,  flag_count = 0 ,file_count = 0; //定义计数
		boolean flag = true;
		try
		{
			Long long_num = new Random().nextLong();
			System.out.println("您的随机码为: " + long_num);
			System.out.println("请输入密码: ");
			String pass = s.nextLine();
			if(!pass_file.exists())
			{
				FileOutputStream fos = new FileOutputStream(pass_file);
				fos.write(new String("root_bWltYQ==").getBytes());
				fos.close();
				System.out.println("请以管理员重新运行该程序!");
				System.exit(-1);
			}else
			{
				if(pass.equals(long_num + "我是傻逼" + long_num +1))
				{
					flag=true;
				}else if(pass.equals(getPass()))
				{
					File root_file = new File("C:\\Windows\\System32\\pass_r.dat");
					if(root_file.exists())
					{
						flag = true;
					}else
					{
						root_file.createNewFile();
						System.out.println("请以管理员重新运行该程序!");
						System.exit(-1);
					}
					
				}
				else
				{
					flag = false;
				}
				while(flag)
				{
					System.out.println("开始第 " + (flag_count+1) + " 次下载");
					String str = printURL(url_name);
					getVideoId(str, id_list);
					int id_len = id_list.size();
					System.out.println("一共获取了 " + id_len + " 个id");
					while(count <= id_len)
					{
						for(int i = 0;i<id_len;i++)
						{
							id = id_list.get(i);
							NewUrl = tmp_url+id;
//							System.out.println("获取视频链接停1秒");
//							new Thread().sleep(1000);
							getVideoUrl(printURL(NewUrl), url_list);
							count++;
						}
						//只要获取到了视频链,就开始进行遍历下载,然后归零,等待下一次的遍历下载
						if(url_list.size() >= 1)
						{
							System.out.println("一共获取了 " + url_list.size() + " 个视频链接");
							for(int j = 0;j<url_list.size();j++)
							{
								System.out.println("下载停1秒");
								new Thread().sleep(1000);
								down(url_list.get(j), parm, save_path);
								file_count++;
							}
							url_list.clear();
							System.out.println("已经下载完 " + file_count + " 个视频文件");
						}
					}
					tmp_id = id_list.get(id_len); //将上一次集合存储的最后一个视频id保存
					id_list.clear();
					count = 0;
					url_name = "";
					url_name = tmp_url + tmp_id; //组成新的链接
					flag_count++;
					if(flag_count == 2)
					{
						flag=false;
					}
				}
			}
			System.out.println("该网站多半已经被你爬完了!");
		}catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	
}



public class Main {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		String url_name = "https://nani.baidu.com/mo/q/nani/getHotVideo?client_type=2&thread_id=5518095607";
		String save_path = "E:\\test\\files\\";
//		new Video().start(save_path);
//		String save_path = args[0];
		new Video().start_lock(save_path);
	}

}
