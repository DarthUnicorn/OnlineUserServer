package onlineuserserver;
  
import java.io.File;
import java.io.Serializable; 
import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.ArrayList;

public class User implements Serializable {
    
    private String ip, id;
    private int TCPServerPort;   
    private int UDPServerPort; 
    
    public User(String id, int UDPServerPort, int TCPServerPort) throws UnknownHostException
    {
        this.id = id;
        this.ip = Inet4Address.getLocalHost().getHostAddress();
        this.TCPServerPort = TCPServerPort;
        this.UDPServerPort = UDPServerPort; 
    }
    
    public User(String id) throws UnknownHostException
    {
        this.id = id;
        this.ip = Inet4Address.getLocalHost().getHostAddress();
        this.TCPServerPort = 6001;
        this.UDPServerPort = 7001; 
    }  
    
    //Getters
    public String getIp() {
        return ip;
    }
 
    public void setIp(String ip) {
        this.ip = ip;
    }
 
    public int getTCPServerPort() {
        return TCPServerPort;
    }
 
    public void setTCPServerPort(int TCPServerPort) {
        this.TCPServerPort = TCPServerPort;
    }
 
    public int getUDPServerPort() {
        return UDPServerPort;
    }
 
    public void setUDPServerPort(int UDPServerPort) {
        this.UDPServerPort = UDPServerPort;
    }
    
    //Methods 
    public ArrayList<File> getFiles2Share(String folderPath) {
        
        ArrayList<File> localFiles = new ArrayList<>();
        File currentDirectory = new File(folderPath);
        
        for(File file : currentDirectory.listFiles())
            localFiles.add(file);
        
        return localFiles;
    }
     
    
    @Override
    public String toString()
    {
        String output = id 
                + "," + ip 
                + "," + UDPServerPort
                + "," + TCPServerPort;
        
        return output + "\n";
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }
  
}
