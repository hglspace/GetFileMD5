package com.cn.org;

import java.io.FileInputStream;
import java.io.IOException;
import java.security.DigestInputStream;
import java.security.MessageDigest;

public class Test {

	public static void main(String[] args) {
		String filePath="/Users/test/Applications/error.txt";
		System.out.println(fileMD5(filePath));
	}
	public static String fileMD5(String filePath){
		int bufferSize=1024*1024;//设置缓冲区大小
		FileInputStream fis = null;
		DigestInputStream dis = null;
		try{
			MessageDigest md = MessageDigest.getInstance("MD5");
			fis = new FileInputStream(filePath);
			dis = new DigestInputStream(fis,md);
			byte[] buffer = new byte[bufferSize];
			while(dis.read(buffer)>0){//read的过程对文件进行MD5处理
				md = dis.getMessageDigest();//获取最终的MessageDigest对象
				byte[] resultByteArray = md.digest();//这个数组的长度是16
				return byteArrayToHex(resultByteArray);
			}
		}catch(Exception e){
			
		}finally{
			try {
				if(dis!=null){
					dis.close();
				}
				if(fis!=null){
					fis.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static String byteArrayToHex(byte[] byteArray){
		//初始化一个字符数组，用来存放每个16进制字符
		char[] hexDigits = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		char[] resultCharArray = new char[byteArray.length*2];
		int index = 0;
		for(byte b : byteArray){
			/*
			 * 一个byte是八位二进制数，十六进制数转化为二进制数时，比如F，结果是1111,所以八位二进制数可以转为两位的十六进制数
			 * byteArray的长度是16位的，要转换为标准的32位MD5值，所以每一个八进制要扩展为2位的16进制数，所以resultCharArray
			 * 的长度为byteArray长度的两倍
			 * 位运算的效率比较高，所以选用了位运算
			 * >>> 无符号右移，把二进制数向右移动n位，最高位空出来的全部用零补位，低位移除的舍弃
			 * b>>>4 首先b会转化为8位二进制数，然后右移4位，取得b的高四位
			 */
			//先把b的高四位转为16进制数,和0xf(十六进制数f)做与运算，排除负数的干扰
			resultCharArray[index++] = hexDigits[b>>>4 & 0xf];
			//再把b的低四位转为16进制数 1000 1111 & 0000 1111 = 0000 1111
			resultCharArray[index++] = hexDigits[b & 0xf];
		}
		return new String(resultCharArray);
	}
}
