import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;

import static java.lang.Thread.sleep;

public class MainConsole {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder();
        Scanner scanner = new Scanner(System.in);
        String command;
        Main main = new Main();

        do {
            if (args != null && args.length > 0) {
                for (String arg : args) {
                    sb.append(arg).append(" ");
                }
                command = sb.toString();
            }else {
                System.out.println("Type the address you want to ping with parameters. Type -h to get help.:");
                command = scanner.nextLine();
            }
        }while (command.trim().length() <= 0);



        boolean infinite = false;
        int limit = 5;
        int ttl = 128;
        int timeout = 5000;
        NetworkInterface defaultNIC = null;
        boolean soundIfSucceeded = false;
        boolean soundIfFailed = false;
        boolean soundIfStateChanged = false;




        if (!command.trim().equalsIgnoreCase("exit") ){
            String[] lines = command.split("\s(?=-)");

            for (String line : lines) {
                if (line.contains("-h")){
                    System.out.println(main.getHelp());
                    main(null);
                }


                if (line.contains("-t")) {
                    infinite = true;
                    limit = -1;
                } else if (command.contains("-n")) {
                        infinite = false;
                        String s = command.substring(command.indexOf("-n") + 2);
                        if (s.contains("-"))
                            limit = Integer.parseInt(s.substring(0, s.indexOf('-')).trim());
                        else
                            limit = Integer.parseInt(s.trim());
                }



                if (line.contains("-i")){
                    String s = command.substring(command.indexOf("-i") + 2);
                    if (s.contains("-"))
                        ttl = Integer.parseInt(s.substring(0, s.indexOf('-')).trim());
                    else
                        ttl = Integer.parseInt(s.trim());
                }

                if (line.contains("-w")){
                    String s = command.substring(command.indexOf("-w") + 2);
                    if (s.contains("-"))
                        timeout = Integer.parseInt(s.substring(0, s.indexOf('-')).trim());
                    else
                        timeout = Integer.parseInt(s.trim());
                }

                if (line.contains("-s")){
                    soundIfSucceeded = true;
                }

                if (line.contains("-f")){
                    soundIfFailed = true;
                }

                if (line.contains("-c")){
                    soundIfSucceeded = soundIfFailed = false;
                    soundIfStateChanged = true;
                }

                if (line.contains("-S")){
                    String s = command.substring(command.indexOf("-S") + 2);
                    if (s.contains("-")) {
                        String desired_Source = s.substring(0, s.indexOf('-')).trim();
                        for (NIC nic : parseMyNICs()) {
                            for (IpAddress ipAddress : nic.ipAddresses) {
                                if (ipAddress.getIpSfof().equalsIgnoreCase(desired_Source)) {
                                    defaultNIC = nic.networkInterface;
                                    break;
                                }
                            }
                        }

                    } else {
                        String desired_Source = s.trim();
                        for (NIC nic : parseMyNICs()) {
                            for (IpAddress ipAddress : nic.ipAddresses) {
                                if (ipAddress.getIpSfof().equalsIgnoreCase(desired_Source)) {
                                    defaultNIC = nic.networkInterface;
                                    break;
                                }
                            }
                        }
                    }
                }
            }


            String ip = lines[0].trim();
            IpAddress otherIpAddress = new IpAddress(ip);
            if (defaultNIC == null) {
                for (NIC myNIC : parseMyNICs()) {
                    for (IpAddress myIpAddress : myNIC.ipAddresses) {
                        boolean isMineIPV4 = (boolean) IpAddress.isIPV4(myIpAddress.getIpSfof())[0];
                        boolean isOtherIPV4 = (boolean) IpAddress.isIPV4(otherIpAddress.getIpSfof())[0];

                        boolean areOnSameNet = false;
                        AddressFactory factory = new AddressFactory();

                        if (isMineIPV4 && isOtherIPV4) areOnSameNet = factory.areOnSameNetwork(myIpAddress, otherIpAddress);

                        if (areOnSameNet) {
                            defaultNIC = myNIC.networkInterface;
                        }
                    }

                }
            }

            boolean finalInfinite = infinite;
            int finalLimit = limit;
            NetworkInterface finalDefaultNIC = defaultNIC;
            int finalTtl = ttl;
            int finalTimeout = timeout;
            boolean finalSoundIfFailed = soundIfFailed;
            boolean finalSoundIfSucceeded = soundIfSucceeded;
            boolean finalSoundIfStateChanged = soundIfStateChanged;
            new Thread(() -> {
                try {
                    System.out.println("\nSending Ping Request to " + otherIpAddress.getIpSfof() + ":");
                    if (!finalInfinite) {
                        for (int i = 0; i < finalLimit; i++) {

                            System.out.print(main.sendPingRequest(finalDefaultNIC, otherIpAddress.getIpSfof(), finalTtl, finalTimeout, finalSoundIfSucceeded, finalSoundIfFailed, finalSoundIfStateChanged));
                        }
                    } else {
                        while (true) {
                            System.out.print(main.sendPingRequest(finalDefaultNIC, otherIpAddress.getIpSfof(), finalTtl, finalTimeout, finalSoundIfSucceeded, finalSoundIfFailed, finalSoundIfStateChanged));
                        }
                    }
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            main(null);
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();


        }




    }

    private static ArrayList<NIC> parseMyNICs() {
        ArrayList<NIC> nics = new ArrayList<>();
        try {
            List<NetworkInterface> interfaces = Collections.list(NetworkInterface.getNetworkInterfaces());
            for (NetworkInterface n : interfaces) {
                try {
                    if (n.getInterfaceAddresses() != null && n.getInterfaceAddresses().size() > 0) {
                        NIC nic = new NIC();
                        nic.networkInterface = n;
                        for (InterfaceAddress interfaceAddress : n.getInterfaceAddresses()) {
                            String address = interfaceAddress.getAddress().getHostAddress() + "/" + interfaceAddress.getNetworkPrefixLength();
                            nic.ipAddresses.add(new IpAddress(address));
                        }
                        nics.add(nic);

                    }
                } catch (Exception ignored) {
                }
            }


        } catch (SocketException e) {
            e.printStackTrace();
        }
        return nics;
    }
}
