package work.mariomoreno.edumanager.attendance;

import java.util.List;

/**
 * Frontera hacia el almacen analitico. La version original apuntaba a AWS S3/Redshift;
 * la unica implementacion incluida lee de la base local (H2). Una implementacion
 * Redshift/S3 se enchufa aqui sin tocar controladores.
 */
public interface AttendanceReportSource {
    List<StudentAttendanceSummary> summaryByStudent();
}
