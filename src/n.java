import java.util.Scanner;

public class n {
    public static void main(String[] args) {
        String string = new Scanner(System.in).nextLine();
        for (char c : string.toCharArray()) {
            System.out.println("c = " + c);
            int s = c;
            System.out.println("s = " + s);
        }
    }

    private static IpAddress nextAddress(IpAddress startIp, int times) {
        IpAddress increased = startIp;
        for (int i = 0; i < times; i++) {
            increased = nextAddress(increased);
        }
        return (increased);
    }

    static IpAddress nextAddress(IpAddress startIp) {
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
    private static int power(int num, int pwr) {
        System.out.println("power = " + num + "^" + pwr);
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
}
