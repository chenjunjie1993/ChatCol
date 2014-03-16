package com.cjj.qq.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class QQClient extends JFrame implements ActionListener
{
	private JTextArea show;
	private JTextField input;
	private JScrollPane sp;
	private JPanel eastPanel;
	
	private Socket client;
	private OutputStream os;
	private InputStream is;
	
	public QQClient()
	{
		show = new JTextArea();
		input = new JTextField();
		sp = new JScrollPane(show);
		
		show.setEditable(false);
		input.addActionListener(this);
		
		getContentPane().add(sp);
		getContentPane().add(input, BorderLayout.SOUTH);
		
		setSize(640, 480);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		
		connectServer();
	}

	private void connectServer()
	{
		try
		{
			client = new Socket("localhost", 8888);
			
			// 获得I/O流
			is = client.getInputStream();
			os = client.getOutputStream();
			
			// 读取数据
			new Thread(new Runnable()
			{
				
				@Override
				public void run()
				{
					while (true)
					{
						try
						{
							byte[] b = new byte[1024];
							int len = is.read(b);
							System.out.println(len);
							String str = new String(b, 0, len);
							show.append(str + '\n');
							
						} catch (IOException e)
						{
							show.append("服务器未响应...\n");
							break;
						}
					}
					
				}
			}).start();
			
		} catch (Exception e)
		{
			show.append("连接失败\n");
		}
		
		
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String text = input.getText();
		if(os != null)
		{
			try
			{
				os.write(text.getBytes());
			} catch (IOException e1)
			{
				e1.printStackTrace();
			}
		}
		show.append("客户端：" + text + '\n');
		input.setText("");
	}
	
}
