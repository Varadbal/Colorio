package ColorioCommon;

import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class KeyEvent implements UDPSerializable, KeyInput {

    private int playerId;
    private char keyChar;
    private long timeStamp;
    private int id;

    public KeyEvent(int playerId, java.awt.event.KeyEvent e){
        this.playerId = playerId;
        keyChar=e.getKeyChar();
        timeStamp=e.getWhen();
        id=e.getID();
    }

    /**
     * Getters
     */
    public int getPlayerId() {return playerId;}

    public int getId(){return id;}

    public long getTimeStamp(){return timeStamp;}

    public char getKeyChar(){return keyChar;}


    /**
     * Overrides
     */
    @Override
    public String toString() {
        return ("Keychar: "+keyChar+" id: "+id);
    }

    @Override
    public DatagramPacket toDatagramPacket(InetAddress address, int port) {
        try {
            // Serializing the packet
            ByteArrayOutputStream baos = new ByteArrayOutputStream(6400);
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(this);
            oos.flush();
            byte[] bytes = baos.toByteArray();
            return new DatagramPacket(bytes,bytes.length,address,port);
        } catch (IOException e) {
            System.out.println("Serialization problem on " + Thread.currentThread().getName());
        }
        return null;
    }

    @Override
    public boolean getFromDatagramPacket(DatagramPacket packet) {
        ByteArrayInputStream bais = new ByteArrayInputStream(packet.getData());

        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(bais);
        } catch (IOException e) {
            System.out.println("ObjectInputStream error: {0}");
            return false;
        }

        KeyEvent recivedPacket = null;
        try {
            recivedPacket = (KeyEvent) ois.readObject();
        } catch (IOException e) {
            System.out.println("Read object error: {0}");
            return false;
        } catch (ClassNotFoundException e) {
            System.out.println("Class not found: {0}");
            return false;
        }
        catch (ClassCastException e){
            System.out.println("Wrong class!");
            return false;
        }
        keyChar=recivedPacket.keyChar;
        timeStamp=recivedPacket.timeStamp;
        return true;
    }
}
