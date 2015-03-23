package util;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author BRAVO
 */
public class AppSql {

    private AppSql() {
    }
    ;
    
    public static final String SQL_ALL_MEMBERS = " SELECT m.id_membre, m.first_name, m.last_name, m.address, upper(s.statut_name) "
            + " FROM membre m JOIN statut s "
            + " ON m.id_statut = s.id ";

    public static final String SQL_ALL_STATUT_MEMBERS = " SELECT m.id_membre, m.first_name, m.last_name, m.address, upper(s.statut_name) "
            + " FROM membre m JOIN statut s "
            + " ON m.id_statut = s.id "
            + " WHERE s.id = ?";

//        public static final String SQL_ALL_STATUT_MEMBERS = " SELECT m.id_membre, m.first_name, m.last_name, m.address, upper(s.statut_name) "
//            + " FROM membre m JOIN statut s "
//            + " ON m.id_statut = s.id "
//            + " WHERE s.id = (SELECT id FROM statut WHERE statut_name= ?)";
    
    public static final String SQL_ID_STATUT = "SELECT id FROM statut WHERE statut_name= ?";

    public static final String DELETE_MEMBER = "DELETE FROM membre WHERE id_membre= ?";

}
