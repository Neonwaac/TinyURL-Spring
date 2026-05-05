package com.demo.demo.application.port.in;

import com.demo.demo.application.port.in.dto.LinkListItem;

import java.util.List;

public interface ListLinksUseCase {
	List<LinkListItem> listAll();
}
