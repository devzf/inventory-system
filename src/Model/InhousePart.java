package Model;

/**
 *
 * @author Zachary Fredricksen #000999825
 */
public class InhousePart extends Part {
    private int machineID;
    
    public void setMachineID(int machineID) {
        this.machineID = machineID;
    }
    
    public int getMachineID() {
        return machineID;
    }
}
