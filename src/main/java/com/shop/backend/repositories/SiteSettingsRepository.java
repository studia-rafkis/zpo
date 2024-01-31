package com.shop.backend.repositories;
import com.shop.backend.entities.CommentsLikes;
import com.shop.backend.entities.SiteSettings;
import org.springframework.data.jpa.repository.JpaRepository;
public interface SiteSettingsRepository extends JpaRepository<SiteSettings, Long> {
}
