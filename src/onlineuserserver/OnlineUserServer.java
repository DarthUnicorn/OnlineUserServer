package onlineuserserver;

import java.io.*;
import java.net.*;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger; 

class OnlineUserServer
{ 
   public static void main(String argv[]) throws Exception
      { 
         ServerSocket socket = new ServerSocket(6789);  
          
         while(true)
         {
             try
             {
                Socket connectionSocket = socket.accept();
                RequestThread newThread = new RequestThread(connectionSocket);
                newThread.start();
             }
             catch(IOException e)
             {
                 System.out.println(e.toString());
             } 
         }
      }
   
   static class RequestThread extends Thread
   {
       private final Socket connectionSocket; 
       private User newUser; 
       private static ArrayList<User> onlineUsers  = new ArrayList<>(); 

       
       public RequestThread(Socket connectionSocket)
       {
           this.connectionSocket = connectionSocket;
       }
       
       @Override
       public void run() 
       {
           try {
                //getting bytes from client 
                InputStream stream = connectionSocket.getInputStream();   
                ByteBuffer bb = ByteBuffer.wrap(new byte[Integer.SIZE]);
                
                int returnType = stream.read(bb.array());
                int lengthFromClient;

                if(returnType == -1) 
                { 
                    connectionSocket.close();
                }
                else
                {
                    lengthFromClient = bb.getInt(); 
                    byte[] actualMessage = new byte[lengthFromClient]; 
                    stream.read(actualMessage, 0, lengthFromClient);  
                    String message = new String(actualMessage); 
                    
                    if(message.equals("REFRESH_REQUEST"))
                    {
                        OutputStream outputStream = connectionSocket.getOutputStream();
                        byte[] toClient = onlineUsers.toString().replace("[","").replace("]","").getBytes(); 
                        int length = toClient.length; 
                        byte[] toClientLength = ByteBuffer.allocate(Integer.SIZE).putInt(length).array();

                        outputStream.write(toClientLength);
                        outputStream.write(toClient);
                    } 
                      else
                    {
                        //creating new User to add to list of Online Users
                        String splitMessage[] = message.split(","); 

                        newUser = new User(splitMessage[0]);
                        newUser.setIp(connectionSocket.getInetAddress().toString().replace("/", ""));
                        newUser.setUDPServerPort(Integer.parseInt(splitMessage[1]));
                        newUser.setTCPServerPort(Integer.parseInt(splitMessage[2]));

                        System.out.println(newUser.toString());

                        //if you try to add the same user, it deletes it from the list
                        boolean userExists = false;
                        int index = 0;

                        for(User user: onlineUsers)
                        {
                            if(user.getId().equals(newUser.getId()) || user.getId().contains("REFRESH_REQUEST"))
                            {
                                userExists = true; 
                                break; 
                            }
                             index++; 
                        }
  
                        if(userExists) 
                            onlineUsers.remove(index); 
                        else
                            onlineUsers.add(newUser);
                        
                        //sending list of all users back to clients   
                        OutputStream outputStream = connectionSocket.getOutputStream();
                        byte[] toClient = onlineUsers.toString().replace("[","").replace("]","").getBytes(); 
                        int length = toClient.length; 
                        byte[] toClientLength = ByteBuffer.allocate(Integer.SIZE).putInt(length).array();

                        outputStream.write(toClientLength);
                        outputStream.write(toClient);

                        System.out.println("Current Online Users");
                        System.out.println("--------------------");
                        System.out.println(onlineUsers.toString().replace("[", "").replace("]", "")); 
                    }
                }
                } catch (IOException ex) 
                {
               Logger.getLogger(OnlineUserServer.class.getName()).log(Level.SEVERE, null, ex);
           }
            
       }
       
   }
   
   
}