package co.unicauca.gestiontrabajogrado.dto.submission;

import java.util.List;

/**
 * DTO para respuesta paginada de Formato A
 * Corresponde a la respuesta del GET /api/submissions/formatoA?docenteId=...&page=...&size=...
 */
public class FormatoAPage {

    private List<FormatoAView> content;
    private int page;
    private int size;
    private long totalElements;
    private int totalPages;
    private boolean first;
    private boolean last;

    public FormatoAPage() {
    }

    // Getters y Setters
    public List<FormatoAView> getContent() {
        return content;
    }

    public void setContent(List<FormatoAView> content) {
        this.content = content;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public long getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(long totalElements) {
        this.totalElements = totalElements;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public boolean isFirst() {
        return first;
    }

    public void setFirst(boolean first) {
        this.first = first;
    }

    public boolean isLast() {
        return last;
    }

    public void setLast(boolean last) {
        this.last = last;
    }
}

