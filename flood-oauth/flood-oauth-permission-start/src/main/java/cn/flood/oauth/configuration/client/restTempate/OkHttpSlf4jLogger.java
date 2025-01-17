/**
 * Copyright (c) 2018-2028,
 * <p>
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE 3.0; you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/lgpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package cn.flood.oauth.configuration.client.restTempate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OkHttp Slf4j logger
 *
 * @author mmdai
 */
public class OkHttpSlf4jLogger implements HttpRestLoggingInterceptor.Logger {

  private final Logger log = LoggerFactory.getLogger(this.getClass());

  @Override
  public void log(String message) {
    log.info(message);
  }
}
