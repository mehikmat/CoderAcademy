package demo.cassandra.util;

import javax.swing.*;
import java.awt.*;

public class Center
{
    public Center(JFrame f,int x,int y)
    {
        f.setSize(x,y);

        Dimension screenSize=Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize=f.getSize();

        if(frameSize.height>screenSize.height)
            frameSize.height  =  screenSize.height;

        if(frameSize.width>screenSize.width)
            frameSize.width  =   screenSize.width;

        f.setLocation((screenSize.width-frameSize.width)/2 ,  (screenSize.height-frameSize.height)/2  );
    }
}
        