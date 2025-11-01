package co.unicauca.gestiontrabajogrado.presentation.dashboard.jefedepartamentoview;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * TableModel para anteproyectos del Jefe de Departamento
 */
public class AnteproyectoTableModel extends AbstractTableModel {

    private final List<AnteproyectoRow> rows = new ArrayList<>();
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd MMM yyyy");

    private static final String[] COLUMNS = {
            "Docente", "Nombre del Anteproyecto", "Fecha de Subida", "Estado", "Acción"
    };

    // ====== API pública ======

    public void setRows(List<AnteproyectoRow> data) {
        rows.clear();
        if (data != null) rows.addAll(data);
        fireTableDataChanged();
    }

    public AnteproyectoRow getRow(int rowIndex) {
        return rows.get(rowIndex);
    }

    public void updateRow(int rowIndex, AnteproyectoRow newRow) {
        rows.set(rowIndex, newRow);
        fireTableRowsUpdated(rowIndex, rowIndex);
    }

    public long countByEstado(String estado) {
        return rows.stream()
                .filter(r -> estado.equalsIgnoreCase(r.estado()))
                .count();
    }

    // ====== AbstractTableModel ======

    @Override
    public int getRowCount() {
        return rows.size();
    }

    @Override
    public int getColumnCount() {
        return COLUMNS.length;
    }

    @Override
    public String getColumnName(int column) {
        return COLUMNS[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AnteproyectoRow r = rows.get(rowIndex);
        return switch (columnIndex) {
            case 0 -> r.nombreDocente();
            case 1 -> r.titulo();
            case 2 -> r.fechaSubida() != null ?
                    r.fechaSubida().format(DATE_FORMATTER) : "—";
            case 3 -> r.estado();
            case 4 -> null; // Columna de acción (botones)
            default -> "";
        };
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 4; // Solo columna de acción
    }
}