import javax.sound.sampled.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;

public class Main {
    String getHelp(){
        return """

                Usage: address [-t] [-s] [-f] [-c] [-n count] [-i TTL] [-w timeout] [-S srcaddr]

                Options:
                    -t             Ping the specified host until stopped.
                    -s             Play sound when host is reachable.
                    -f             Play sound when host is unreachable.
                    -c             Play sound only when host state changes between reachable and unreachable.
                    -n count       Number of echo requests to send.
                    -i TTL         Time To Live.
                    -w timeout     Timeout in milliseconds to wait for each reply.
                    -S srcaddr     Source address to use.
                    
                    
                Example: 192.168.168.1 -t -c -S 192.168.168.2
                
                    """;
    }
    String sendPingRequest(String ipAddress) throws IOException {

        return sendPingRequest(null, ipAddress, 128, 5000, false, false, false);
    }

    private char state;
    String sendPingRequest(NetworkInterface networkInterface, String ipAddress, int ttl, int timeout, boolean successSound, boolean failedSound, boolean stateSound) throws IOException {
        InetAddress address = InetAddress.getByName(ipAddress);
        long start = System.currentTimeMillis(), end;
        if (address.isReachable(networkInterface, ttl, timeout)) {
            end = System.currentTimeMillis();
            String time = (end - start)<1 ? "<" + (end - start) + "ms" : "=" + (end - start) + "ms" ;
            if (successSound){
                Toolkit.getDefaultToolkit().beep();
            }

            if ('u' == state && stateSound){
                Toolkit.getDefaultToolkit().beep();

            }

            state = 'r';
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return ("\tbytes=32 time"+ time + " TTL=" + ttl +"\n");
        } else {
            if (failedSound){
                Toolkit.getDefaultToolkit().beep();
            }

            if ('r' == state && stateSound){
                Toolkit.getDefaultToolkit().beep();

            }

            state = 'u';

            return ("\tDestination host unreachable.\n");
        }
    }

}
