import java.util.ArrayList;

public class IpAddresses extends ArrayList<IpAddress> {

    public boolean contains(IpAddress o) {
        boolean found = false;
        for (IpAddress ipAddress : this){
            if (ipAddress.getIpSfof().equals(o.getIpSfof())){
                found = true; break;
            }
        }
        return found;
    }
}
