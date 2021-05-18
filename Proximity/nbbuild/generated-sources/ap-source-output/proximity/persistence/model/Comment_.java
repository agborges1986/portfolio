package proximity.persistence.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;
import proximity.persistence.model.Category;
import proximity.persistence.model.Place;
import proximity.persistence.model.User;

@Generated(value="EclipseLink-2.4.1.v20121003-rNA", date="2013-06-05T18:33:56")
@StaticMetamodel(Comment.class)
public class Comment_ { 

    public static volatile SingularAttribute<Comment, Category> category;
    public static volatile SingularAttribute<Comment, String> text;
    public static volatile SingularAttribute<Comment, String> image;
    public static volatile SingularAttribute<Comment, Long> creationTime;
    public static volatile SingularAttribute<Comment, Byte> rating;
    public static volatile SingularAttribute<Comment, Place> place;
    public static volatile SingularAttribute<Comment, User> user;
    public static volatile SingularAttribute<Comment, Integer> commentId;

}