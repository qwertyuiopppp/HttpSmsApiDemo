using Newtonsoft.Json.Linq;
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Net.Http;
using System.Text;
using System.Security.Cryptography;
using System.Threading.Tasks;

namespace Xxy.Http
{
	public class XxyHttpClient
	{
		public string mtUrl;
		public string moUrl;
		public string reportUrl;
		public string balanceUrl;
		public string apiKey;
		public string userId;
		HttpClient client = HttpClientFactory.Create();

		public XxyHttpClient(string mtUrl, string moUrl, string reportUrl, string balanceUrl, string apiKey, string userId)
		{
			this.mtUrl = mtUrl;
			this.moUrl = moUrl;
			this.reportUrl = reportUrl;
			this.balanceUrl = balanceUrl;
			this.apiKey = apiKey;
			this.userId = userId;
		}

		public Task<string> GetBalance()
		{
			long timestamp = UnixTimestampMs();
			string sign = Md5_32(userId.ToLower() + timestamp.ToString() + apiKey.ToLower().ToString());
			var url = $"{balanceUrl}?sign={sign}&ts={timestamp}&userid={userId}";
			return client.GetStringAsync(url);
		}

		public Task<string> GetReport()
		{
			long timestamp = UnixTimestampMs();
			string sign = Md5_32(userId.ToLower() + timestamp.ToString() + apiKey.ToLower().ToString());
			var url = $"{reportUrl}?sign={sign}&ts={timestamp}&userid={userId}";
			return client.GetStringAsync(url);
		}

		public Task<string> GetMo()
		{
			long timestamp = UnixTimestampMs();
			string sign = Md5_32(userId.ToLower() + timestamp.ToString() + apiKey.ToLower().ToString());
			var url = $"{moUrl}?sign={sign}&ts={timestamp}&userid={userId}";
			return client.GetStringAsync(url);
		}

		public Task<string> PostMt(string content, string mobiles)
		{
			string result = string.Empty;
			long timestamp = UnixTimestampMs();
			string sign = Md5_32(userId.ToLower() + timestamp.ToString() + apiKey.ToLower().ToString());
			Dictionary<string, string> dic = new Dictionary<string, string>();
			dic.Add("sign", sign);
			dic.Add("ts", timestamp.ToString());
			dic.Add("userid", userId);
			dic.Add("content", content);
			dic.Add("mobile", mobiles);
			HttpContent queryString = new FormUrlEncodedContent(dic);
			return client.PostAsync(mtUrl, queryString).Result.Content.ReadAsStringAsync();
		}

		public static long UnixTimestampMs()
		{
			return (DateTime.Now.ToUniversalTime().Ticks - 621355968000000000) / 10000;
		}

		/// <summary>
		/// 
		/// </summary>
		/// <param name="value"></param>
		/// <param name="lower">是否小写</param>
		/// <returns></returns>
		public static string Md5_32(string value, bool lower = true)
		{
			using (var md5 = MD5.Create())
			{
				var result = md5.ComputeHash(Encoding.UTF8.GetBytes(value));
				string md5_32 = BitConverter.ToString(result).Replace("-", "");
				if (lower)
				{
					return md5_32.ToLower();
				}
				return md5_32;
			}
		}
	}
}
