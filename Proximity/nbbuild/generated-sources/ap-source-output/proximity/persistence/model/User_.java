package proximity.persistence.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import proximity.persistence.model.Category;

@Generated(value="EclipseLink-2.4.1.v20121003-rNA", date="2013-06-05T18:33:56")
@StaticMetamodel(User.class)
public class User_ { 

    public static volatile SingularAttribute<User, Integer> maximumDistance;
    public static volatile SingularAttribute<User, Integer> userId;
    public static volatile SingularAttribute<User, String> userName;
    public static volatile SingularAttribute<User, String> image;
    public static volatile SetAttribute<User, Category> categories;
    public static volatile SingularAttribute<User, String> displayName;
    public static volatile SingularAttribute<User, String> passwordHash;

}