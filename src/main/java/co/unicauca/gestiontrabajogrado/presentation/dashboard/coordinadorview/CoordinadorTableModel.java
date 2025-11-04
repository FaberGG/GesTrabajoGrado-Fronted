package co.unicauca.gestiontrabajogrado.presentation.dashboard.coordinadorview;

import javax.swing.table.AbstractTableModel;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel del panel del Coordinador (RF3)
 * Migrado para eliminar dependencias de enums del monolito
 */
public class CoordinadorTableModel extends AbstractTableModel {

    private final List<PropuestaRow> rows = new ArrayList<>();
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static final String[] COLS = {
            "Título", "Director", "Fecha Carga", "Estado", "Acciones"
    };

    // -------- API usada desde CoordinadorView --------

    /** Reemplaza el contenido completo de la tabla. */
    public void setRows(List<PropuestaRow> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    /** Devuelve la fila original. */
    public PropuestaRow getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    /** Actualiza SOLO el estado de una fila (creando un nuevo record). */
    public void updateEstado(int rowIndex, String nuevoEstado) {
        PropuestaRow old = getRow(rowIndex);
        PropuestaRow upd = old.withEstado(nuevoEstado);
        rows.set(rowIndex, upd);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    /** Cuenta filas por estado */
    public long countBy(String estado) {
        return rows.stream()
                .filter(r -> estado.equalsIgnoreCase(r.estado()))
                .count();
    }

    // -------- AbstractTableModel --------

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLS[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        PropuestaRow r = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> r.titulo();
            case 1 -> r.nombreDocente();
            case 2 -> r.fechaCarga() != null ? r.fechaCarga().format(DATE_FORMATTER) : "—";
            case 3 -> r.estado();
            case 4 -> null; // Acciones (la celda la maneja el renderer/botón)
            default -> "";
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        // Solo la columna Acciones
        return columnIndex == 4;
    }
}
