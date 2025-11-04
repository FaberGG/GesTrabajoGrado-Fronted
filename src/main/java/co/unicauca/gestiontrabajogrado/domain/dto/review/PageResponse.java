package co.unicauca.gestiontrabajogrado.domain.dto.review;

import java.util.List;

/**
 * DTO para respuesta paginada del Review Service
 * @param <T> Tipo de contenido de la página
 */
public class PageResponse<T> {
    private List<T> content;
    private PageableInfo pageable;
    private Integer totalElements;
    private Integer totalPages;

    public PageResponse() {
    }

    public PageResponse(List<T> content, PageableInfo pageable, Integer totalElements, Integer totalPages) {
        this.content = content;
        this.pageable = pageable;
        this.totalElements = totalElements;
        this.totalPages = totalPages;
    }

    public List<T> getContent() {
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }

    public PageableInfo getPageable() {
        return pageable;
    }

    public void setPageable(PageableInfo pageable) {
        this.pageable = pageable;
    }

    public Integer getTotalElements() {
        return totalElements;
    }

    public void setTotalElements(Integer totalElements) {
        this.totalElements = totalElements;
    }

    public Integer getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     * Información de paginación
     */
    public static class PageableInfo {
        private Integer pageNumber;
        private Integer pageSize;

        public PageableInfo() {
        }

        public PageableInfo(Integer pageNumber, Integer pageSize) {
            this.pageNumber = pageNumber;
            this.pageSize = pageSize;
        }

        public Integer getPageNumber() {
            return pageNumber;
        }

        public void setPageNumber(Integer pageNumber) {
            this.pageNumber = pageNumber;
        }

        public Integer getPageSize() {
            return pageSize;
        }

        public void setPageSize(Integer pageSize) {
            this.pageSize = pageSize;
        }
    }
}

