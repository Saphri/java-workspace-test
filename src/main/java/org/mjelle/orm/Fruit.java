package org.mjelle.orm;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;
import io.quarkus.hibernate.reactive.panache.PanacheEntity;

@Entity
@Cacheable
@NoArgsConstructor
public class Fruit extends PanacheEntity {
  @Column(length = 40, unique = true)
  public String name;

  public Fruit(String name) {
      this.name = name;
  }

}
