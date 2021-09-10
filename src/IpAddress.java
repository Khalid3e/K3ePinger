public class IpAddress {
    private String ipSfof;
    private int mask =0;
    private String[] bytes = new String[4];

    static Object[] isIPV4(String ip){
        Object[] toRe = new Object[2];
        if (!ip.contains(".")) {
            toRe[0] = false ;
            return toRe;
        }

        String[] split = AddressFactory.split(ip, '.');
        if (split.length != 4) {
            toRe[0] = false ;
            return toRe;
        }
        for (String octet : split) {
            try {
                int num = Integer.parseInt(octet);
                if (num > 255 || num < 0) {
                    toRe[0] = false ;
                    toRe[1] = octet;
                    return toRe;
                }
            } catch (Exception ignored) {
                toRe[0] = false;
                toRe[1] = octet;
                return toRe;
            }
        }
        toRe[0] = true;
        return toRe;
    }


    IpAddress(String ip){
        int mask = 0;
        if (ip.contains("/")) {
            mask = Integer.parseInt(ip.substring(ip.indexOf('/')+1));
            setMask(mask);
            ipSfof = ip.substring(0, ip.indexOf('/'));
        }else
            ipSfof = ip;

        String[] bytes = AddressFactory.split(ipSfof, '.');
        for (int i = 0, splitLength = bytes.length; i < splitLength; i++) {
            String octet = bytes[i];
            this.bytes[i] = octet;
        }
    }

    @Override
    public String toString() {
        return ipSfof;
    }

    public void setBytes(String[] bytes) {
        this.bytes = bytes;
    }

    public void setIpSfof(String ipSfof) {
        this.ipSfof = ipSfof;
    }

    public void setOctets(String ip) {
        String[] split = AddressFactory.split(ip, '.');
        for (int i = 0, splitLength = split.length; i < splitLength; i++) {
            String octet = split[i];
            bytes[i] = octet;
        }
    }

    public String getIpSfof() {
        return ipSfof;
    }

    String[] getBytes() {
        return bytes;
    }

    IpAddress toBinary(){
        StringBuilder binary = new StringBuilder();
        String[] strings = getBytes();
        for (int i = 0; i < strings.length; i++) {
            String octet = strings[i];
            binary.append(octetToBinary(Integer.parseInt(octet)));
            if (i < 3) binary.append(".");
        }

        return new IpAddress(binary.toString());
    }


    String binaryWithoutPoints(){
        StringBuilder toRe = new StringBuilder();

        for (String octet : getBytes()) {
            toRe.append(octet);
        }

        return toRe.toString();
    }

    private String octetToBinary(int num) {
        int last, i = 0;
        int[] lst = new int[10];
        StringBuilder toReturn = new StringBuilder();
        while (num > 0) {

            last = num % 2;

            num = num / 2;
            lst[i] = (last);
            i++;
        }

        //System.out.println("=");
        int j = i - 1;
        while (j >= 0) {
            //System.out.print("" + lst[j]);
            toReturn.append(lst[j]);
            j--;
        }

        while (toReturn.length() < 8) {
            toReturn.insert(0, '0');
        }


        return toReturn.toString();
    }

    public int getMask() {
        return mask;
    }

    public void setMask(int mask) {
        this.mask = mask;
    }
}
