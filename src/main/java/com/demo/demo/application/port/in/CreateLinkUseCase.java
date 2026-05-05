package com.demo.demo.application.port.in;

import com.demo.demo.application.port.in.dto.CreateLinkCommand;
import com.demo.demo.application.port.in.dto.LinkCreatedView;

public interface CreateLinkUseCase {
	LinkCreatedView create(CreateLinkCommand command);
}
