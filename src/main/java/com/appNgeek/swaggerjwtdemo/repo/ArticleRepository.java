package com.appNgeek.swaggerjwtdemo.repo;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.appNgeek.swaggerjwtdemo.domain.Article;
import com.appNgeek.swaggerjwtdemo.domain.User;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

	Article findByTitle(String title);
	
	Collection<Article> findByUser(User user);

}