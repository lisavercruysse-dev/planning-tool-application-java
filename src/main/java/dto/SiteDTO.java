package dto;

public record SiteDTO(
          int id,
          String name,
          String locatie,
          Integer capaciteit,
          String operationeleStatus,
          String productieStatus
) {}
