package com.demo.demo.application.port.in;

import com.demo.demo.application.port.in.dto.LinkEditView;

public interface GetLinkForEditUseCase {
	LinkEditView getByCode(String code);
}
