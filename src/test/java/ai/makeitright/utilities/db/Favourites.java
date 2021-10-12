package ai.makeitright.utilities.db;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DatabaseTable(tableName = "watches")
public class Favourites {

    @DatabaseField(id = true)
    private String id;
    @DatabaseField
    private String zrodlo;
    @DatabaseField
    private boolean isFavourite;
}
