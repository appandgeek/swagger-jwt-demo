package com.appNgeek.swaggerjwtdemo.rest.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.appNgeek.swaggerjwtdemo.domain.Article;
import com.appNgeek.swaggerjwtdemo.exception.BlogAppException;
import com.appNgeek.swaggerjwtdemo.repo.ArticleRepository;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

	@Autowired
	private ArticleRepository articleRepository;

	@GetMapping
	public Page<Article> findAll(Pageable pageable) {
		return articleRepository.findAll(pageable);
	}

	@GetMapping("/find")
	public Article findByTitle(@RequestParam String title) {
		return articleRepository.findByTitle(title);
	}

	@GetMapping("/{id}")
	public Article findOne(@PathVariable Long id) throws BlogAppException {
		Optional<Article> result = articleRepository.findById(id);
		if (result.isPresent())
			return result.get();
		else
			throw new BlogAppException("Article with given id not found");
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Article create(@RequestBody Article article) {
		return articleRepository.save(article);
	}

	@DeleteMapping("/{id}")
	public void delete(@PathVariable Long id) {
		articleRepository.deleteById(id);
	}

}
