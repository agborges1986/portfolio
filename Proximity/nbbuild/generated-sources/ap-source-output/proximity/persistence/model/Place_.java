package proximity.persistence.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import proximity.persistence.model.Comment;

@Generated(value="EclipseLink-2.4.1.v20121003-rNA", date="2013-06-05T18:33:56")
@StaticMetamodel(Place.class)
public class Place_ { 

    public static volatile SingularAttribute<Place, Integer> placeId;
    public static volatile SingularAttribute<Place, String> name;
    public static volatile SingularAttribute<Place, Long> creationTime;
    public static volatile SingularAttribute<Place, Double> longitude;
    public static volatile SingularAttribute<Place, Integer> totalRating;
    public static volatile SingularAttribute<Place, Double> latitude;
    public static volatile SingularAttribute<Place, Integer> cantRating;
    public static volatile SingularAttribute<Place, Long> heatOverTime;
    public static volatile SetAttribute<Place, Comment> comments;

}