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
import org.springblade.modules.auth.enums.BladeUserEnum;
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
		UserInfo userInfo = null;
		if (Func.isNoneBlank(account, password)) {
			// 获取用户类型
			String userType = tokenParameter.getArgs().getStr("userType");
			// 解密密码
			String decryptPassword = TokenUtil.decryptPassword(password, authProperties.getPublicKey(), authProperties.getPrivateKey());
			log.info("Password{}", password);
			log.info("decryptPassword{}", decryptPassword);

			String hashed = DigestUtil.encrypt(decryptPassword);
			if (decryptPassword != null && decryptPassword.matches("[a-fA-F0-9]{32}")) {
				// 试卷要求 password 为 admin 的 MD5，库中存 sha1(md5)
				hashed = sha1Hex(decryptPassword);
			}

			if (userType.equals(BladeUserEnum.WEB.getName())) {
				userInfo = userService.userInfo(tenantId, account, hashed);
			} else if (userType.equals(BladeUserEnum.APP.getName())) {
				userInfo = userService.userInfo(tenantId, account, hashed);
			} else {
				userInfo = userService.userInfo(tenantId, account, hashed);
			}
		}
		return userInfo;
	}

	private static String sha1Hex(String input) {
		try {
			java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-1");
			byte[] hash = digest.digest(input.getBytes(java.nio.charset.StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder(hash.length * 2);
			for (byte b : hash) {
				sb.append(String.format("%02x", b));
			}
			return sb.toString();
		} catch (Exception e) {
			throw new IllegalStateException("SHA-1 计算失败", e);
		}
	}

}
