import java.util.List;

/**
 * Created by aviam on 4/21/2017.
 */
public class CompoundVertex {
    private List<Address> block;

    public CompoundVertex(List<Address> block){
        this.block = block;
    }

    public List<Address> getBlock(){
        return this.block;
    }
}
