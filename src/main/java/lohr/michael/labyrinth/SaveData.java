package lohr.michael.labyrinth;

import lohr.michael.labyrinth.entities.DynamicEntity;
import lohr.michael.labyrinth.math.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Created by Michael Lohr on 16.01.2016.
 *
 * this class is saved to the save file;
 * it contains the name of the level file, the dynamic entities and the player stats (health etc)
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveData implements Serializable {

    private static final long serialVersionUID = 34534565985854856L;

    private String level;
    private DynamicEntity[] entities;
    private PlayerStats playerStats;

}
