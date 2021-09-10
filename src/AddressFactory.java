import java.util.ArrayList;
import java.util.Scanner;

class AddressFactory {


/*    boolean areOnSameNetwork(IpAddress ipAddress1, IpAddress ipAddress2){
        System.out.println("AddressFactory.areOnSameNetwork(" + ipAddress1 + ", " + ipAddress2);
        try {
            String prefix1 = ipAddress1.getIpSfof().substring(0, ipAddress1.getMask());
            String prefix2 = ipAddress2.getIpSfof().substring(0, ipAddress1.getMask());

            System.out.println("prefix1 = " + prefix1);
            System.out.println("prefix2 = " + prefix2);

            if (!prefix1.equals(prefix2)) {
                return false;
            } else {
                int n = power(2, 32 - ipAddress1.getMask());
                IpAddresses addresses = new IpAddresses();
                for (int i = 0; i < n; i++) {
                    IpAddress address = nextAddress(applyMaskAndGetNetwork(ipAddress1, -1), i);
                    addresses.add(address);
                    System.out.println(address);
                }

                return addresses.contains(ipAddress2);
            }

        }catch (Exception ignored) {
            return false;
        }

    }*/

    boolean areOnSameNetwork(IpAddress ipAddress1, IpAddress ipAddress2){
        return applyMaskAndGetNetwork(ipAddress1, -1).getIpSfof().equals(applyMaskAndGetNetwork(ipAddress2, ipAddress1.getMask()).getIpSfof());

    }




     IpAddress applyMaskAndGetNetwork(IpAddress ip, int mask) {
        if (mask == -1) mask = ip.getMask();
        IpAddress binary = ip.toBinary();
        String noPoints = binary.binaryWithoutPoints();

        noPoints = noPoints.substring(0, mask) + "0".repeat(Math.max(0, 32 - mask));

        ip = toDecimal(noPoints);
        return ip;
    }
     IpAddress toDecimal(String binary){
        StringBuilder dec = new StringBuilder();

        for (int i = 0; i < binary.length(); i = i + 8 ) {
            try {
                dec.append(toDec(Integer.parseInt(binary.substring(i, i +8))));
                if (i < 24){
                    dec.append(".");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }


        return new IpAddress(dec.toString());
    }
    private  String toDec(int n) {
        int i = 0;
        int rslt = 0;
        StringBuilder toReturn = new StringBuilder();

        do {
            int digit;
            digit = n % 10;
            rslt = rslt + (int) (digit) * (power(2, i));

            i++;
        } while ((n /= 10) > 0);
        toReturn.append(rslt);
        // System.out.println("\n=" + rslt + "\n*********************************************************\n");
        return toReturn.toString();
    }
    private  int power(int num, int pwr) {
        int f = num, i = 1;
        if (pwr != 0) {
            while (pwr > i) {
                f = f * num;
                i++;
            }
            return f;
        } else {
            return 1;
        }

    }

     static String[] split(String s, char regex) {
        String[] toReturn = new String[4];
        StringBuilder build = new StringBuilder("");
        int i = 0;
        int octet = 0;
        for (char c : s.toCharArray()) {

            if (c != regex) {

                build.append(c);
            } else {
                toReturn[octet] = (build.toString());
                octet++;
                build = new StringBuilder("");
            }

            if ((s.lastIndexOf('.') == i)) {
                build = new StringBuilder(s.substring(s.lastIndexOf(c) + 1));
                toReturn[octet] = (build.toString());
                break;
            }


            i++;
        }


        return toReturn;
    }

    private  IpAddress nextAddress(IpAddress startIp, int times) {
        IpAddress increased = startIp;
        for (int i = 0; i < times; i++) {
            increased = nextAddress(increased);
        }
        return (increased);
    }

     IpAddress nextAddress(IpAddress startIp) {
        StringBuilder s = new StringBuilder();

        String[] split = startIp.getBytes();
        boolean increased = false;
        for (int i = split.length - 1; i >= 0; i--) {
            String octet = split[i];
            if (!increased) {
                int raw = Integer.parseInt(octet);

                if (raw < 255) {
                    raw++;
                    increased = true;
                }else{
                    raw = 0;
                    increased = false;
                }
                s.insert(0, raw);

            } else {
                s.insert(0, octet);

            }
            if (i != 0) s.insert(0, ".");
        }
        return new IpAddress(s.toString());
    }

    private  int[] getClosestTwoWeight(int x) {
        int weight = 2, n = 1;
        int[] toRe = new int[2];

        while (weight < (x + 2)) {
            n++;
            weight = (int) Math.pow(2, n);
        }
        //Returns weight, for example 2^3 = 8, weight is 8
        toRe[0] = weight;
        //Returns n, for example 2^3 = 8, weight is 3
        toRe[1] = n;

        return toRe;
    }
}
