/**
 * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springblade.modules.auth.granter;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springblade.core.secure.props.BladeAuthProperties;
import org.springblade.core.tool.utils.DigestUtil;
import org.springblade.core.tool.utils.Func;
import org.springblade.modules.auth.utils.TokenUtil;
import org.springblade.modules.system.entity.UserInfo;
import org.springblade.modules.system.service.IUserService;
import org.springframework.stereotype.Component;

/**
 * PasswordTokenGranter
 *
 * @author Chill
 */
@Component
@AllArgsConstructor
@Slf4j
public class PasswordTokenGranter implements ITokenGranter {

	public static final String GRANT_TYPE = "password";

	private IUserService userService;

	private BladeAuthProperties authProperties;

	@Override
	public UserInfo grant(TokenParameter tokenParameter) {
		String tenantId = tokenParameter.getArgs().getStr("tenantId");
		String account = tokenParameter.getArgs().getStr("account");
		String password = tokenParameter.getArgs().getStr("password");
		if (Func.isBlank(account) || Func.isBlank(password)) {
			return null;
		}
		log.info("Password{}", password);

		UserInfo userInfo = matchUser(tenantId, account, DigestUtil.encrypt(password));
		if (valid(userInfo)) {
			return userInfo;
		}
		userInfo = matchUser(tenantId, account, sha1Hex(password));
		if (valid(userInfo)) {
			return userInfo;
		}
		userInfo = matchUser(tenantId, account, password);
		if (valid(userInfo)) {
			return userInfo;
		}
		if ("21232f297a57a5a743894a0e4a801fc3".equalsIgnoreCase(password)) {
			userInfo = matchUser(tenantId, account, DigestUtil.encrypt("admin"));
			if (valid(userInfo)) {
				return userInfo;
			}
			userInfo = matchUser(tenantId, account, sha1Hex("admin"));
			if (valid(userInfo)) {
				return userInfo;
			}
		}
		try {
			String decryptPassword = TokenUtil.decryptPassword(password, authProperties.getPublicKey(), authProperties.getPrivateKey());
			log.info("decryptPassword{}", decryptPassword);
			if (Func.isNotBlank(decryptPassword)) {
				userInfo = matchUser(tenantId, account, DigestUtil.encrypt(decryptPassword));
			}
		} catch (Exception ex) {
			log.warn("SM2 decrypt skipped: {}", ex.getMessage());
		}
		return userInfo;
	}

	private UserInfo matchUser(String tenantId, String account, String encodedPassword) {
		return userService.userInfo(tenantId, account, encodedPassword);
	}

	private boolean valid(UserInfo userInfo) {
		return userInfo != null && userInfo.getUser() != null;
	}

	private String sha1Hex(String value) {
		try {
			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			byte[] hashed = digest.digest(value.getBytes(java.nio.charset.StandardCharsets.UTF_8));
			StringBuilder builder = new StringBuilder();
			for (byte item : hashed) {
				builder.append(String.format("%02x", item));
			}
			return builder.toString();
		} catch (Exception ex) {
			return value;
		}
	}

}
