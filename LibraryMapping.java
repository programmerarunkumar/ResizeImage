import com.sun.jna.Library;
import com.sun.jna.Native;

public interface LibraryMapping extends Library{

    public void open();
    public String resize(String oldImage,String newImage);
    public void close();
    
}
