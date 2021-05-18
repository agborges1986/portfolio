/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */

package org.apache.http.entity.mime;

import org.apache.http.entity.mime.content.__ContentBody;

/**
 * __FormBodyPart class represents a content body that can be used as a part of
 * multipart encoded entities. This class automatically populates the header
 * with standard fields based on the content description of the enclosed body.
 * 
 * @since 4.0
 */
public class __FormBodyPart {

	private final String name;
	private final __Header header;

	private final __ContentBody body;

	public __FormBodyPart(final String name, final __ContentBody body) {
		super();
		if (name == null) {
			throw new IllegalArgumentException("Name may not be null");
		}
		if (body == null) {
			throw new IllegalArgumentException("Body may not be null");
		}
		this.name = name;
		this.body = body;
		this.header = new __Header();

		generateContentDisp(body);
		generateContentType(body);
		generateTransferEncoding(body);
	}

	public String getName() {
		return this.name;
	}

	public __ContentBody getBody() {
		return this.body;
	}

	public __Header getHeader() {
		return this.header;
	}

	public void addField(final String name, final String value) {
		if (name == null) {
			throw new IllegalArgumentException("Field name may not be null");
		}
		this.header.addField(new __MinimalField(name, value));
	}

	protected void generateContentDisp(final __ContentBody body) {
		StringBuilder buffer = new StringBuilder();
		buffer.append("form-data; name=\"");
		buffer.append(getName());
		buffer.append("\"");
		if (body.getFilename() != null) {
			buffer.append("; filename=\"");
			buffer.append(body.getFilename());
			buffer.append("\"");
		}
		addField(__MIME.CONTENT_DISPOSITION, buffer.toString());
	}

	protected void generateContentType(final __ContentBody body) {
		StringBuilder buffer = new StringBuilder();
		buffer.append(body.getMimeType()); // MimeType cannot be null
		if (body.getCharset() != null) { // charset may legitimately be null
			buffer.append("; charset=");
			buffer.append(body.getCharset());
		}
		addField(__MIME.CONTENT_TYPE, buffer.toString());
	}

	protected void generateTransferEncoding(final __ContentBody body) {
		addField(__MIME.CONTENT_TRANSFER_ENC, body.getTransferEncoding()); // TE
																			// cannot
																			// be
																			// null
	}

}
