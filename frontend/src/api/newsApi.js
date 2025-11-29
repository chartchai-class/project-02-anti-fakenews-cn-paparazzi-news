// Mock data for demonstration when backend is not running
const mockNews = [
  {
    id: 1,
    title: "Global Climate Change: Latest Research Shows Worsening Situation",
    summary: "The latest climate research report indicates that global warming is accelerating, with increasing frequency of extreme weather events.",
    content: "<p>According to the latest report released by the United Nations Framework Convention on Climate Change (UNFCCC), global temperatures continue to rise, exceeding pre-industrial levels by 1.2°C.</p><p>The report states that without immediate positive measures, temperatures could rise by more than 2.7°C by the end of the century, leading to more severe extreme weather events, including heatwaves, floods, and droughts.</p><p>Scientists call on countries to strengthen cooperation, reduce carbon emissions, and accelerate the transition to renewable energy.</p>",
    imageUrl: "https://picsum.photos/id/1/800/450",
    trustScore: 92,
    trustLevel: "high",
    source: "Climate Change Research Center",
    date: "2024-03-15T08:00:00Z",
    category: "Environment"
  },
  {
    id: 2,
    title: "2024 Election: Comparison of Main Candidates' Policy Proposals",
    summary: "As the election approaches, the policy proposals of the main candidates have become the focus of public attention.",
    content: "<p>The 2024 election campaign has entered a heated phase, with main candidates engaging in intense debates around issues such as economy, healthcare, and education.</p><p>Candidate A promises to lower tax rates to promote economic growth; Candidate B emphasizes expanding medical insurance coverage; Candidate C proposes a new education reform plan.</p><p>Experts analyze that the outcome of this election will have a profound impact on the country's development direction for the next four years.</p>",
    imageUrl: "https://picsum.photos/id/2/800/450",
    trustScore: 85,
    trustLevel: "high",
    source: "National Political Research Institute",
    date: "2024-03-14T14:30:00Z",
    category: "Politics"
  },
  {
    id: 3,
    title: "New Coronavirus Variant Emerges, Experts Advise Against Panic",
    summary: "Scientists discover new variant of COVID-19, but existing vaccines remain protective.",
    content: "<p>The World Health Organization (WHO) announced today that a new variant of the coronavirus has been found in several countries, but current evidence suggests its transmissibility and pathogenicity have not significantly increased.</p><p>Public health experts from various countries emphasize that existing vaccines still have good effects in preventing severe cases and recommend eligible populations to receive booster shots in a timely manner.</p><p>At the same time, experts remind people to continue maintaining good personal hygiene habits, such as wearing masks, washing hands frequently, and maintaining social distance.</p>",
    imageUrl: "https://picsum.photos/id/3/800/450",
    trustScore: 95,
    trustLevel: "high",
    source: "World Health Organization",
    date: "2024-03-13T10:15:00Z",
    category: "Society"
  },
  {
    id: 4,
    title: "AI Breakthrough: New Model Performs Excellently in Medical Diagnosis",
    summary: "The latest AI model exceeds human experts in disease diagnosis accuracy.",
    content: "<p>A new artificial intelligence model developed by researchers has performed excellently in early cancer screening tests, achieving an accuracy rate of 94%, surpassing the average level of human experts.</p><p>This technology uses deep learning to analyze medical images and can detect subtle abnormal patterns. The research team says this breakthrough is expected to significantly improve early diagnosis rates and save more lives.</p><p>Currently, this model is undergoing clinical trials in multiple hospitals and is expected to be put into practical application within 2-3 years.</p>",
    imageUrl: "https://picsum.photos/id/4/800/450",
    trustScore: 88,
    trustLevel: "high",
    source: "Frontier Science and Technology Magazine",
    date: "2024-03-12T16:45:00Z",
    category: "Education"
  },
  {
    id: 5,
    title: "Hollywood Star Announces Social Media Exit, Sparks Widespread Discussion",
    summary: "Famous actor announces permanent departure from all social media platforms, calling attention to mental health.",
    content: "<p>An Oscar-winning actor announced through an official statement yesterday that they will permanently withdraw from all social media platforms. The statement indicated that the negative impacts of social media have severely affected their mental health and private life.</p>",
    imageUrl: "https://picsum.photos/id/5/800/450",
    trustScore: 75,
    trustLevel: "medium",
    source: "Entertainment Weekly",
    date: "2024-03-11T09:20:00Z",
    category: "Entertainment"
  },
  {
    id: 6,
    title: "Economic Recovery Signs Evident, Experts Predict GDP Growth Will Exceed Expectations",
    summary: "Latest economic data shows both manufacturing and service industry indices trending upward.",
    content: "<p>The latest economic data released by the National Bureau of Statistics shows that the manufacturing PMI index reached 53.2 in the first quarter, remaining in the expansion range for three consecutive months; the service industry PMI index was 55.6, a new high in nearly a year.</p><p>Multiple economists analyze that these data indicate a strong economic recovery momentum, with GDP growth this year expected to reach 5.8%, exceeding the previous expected target of 5.5%.</p><p>At the same time, experts also remind to pay attention to challenges that may be brought by global economic uncertainty and geopolitical risks.</p>",
    imageUrl: "https://picsum.photos/id/6/800/450",
    trustScore: 82,
    trustLevel: "high",
    source: "Economic Observer",
    date: "2024-03-10T11:30:00Z",
    category: "Politics"
  },
  {
    id: 7,
    title: "New Education Reform Policy Released, Emphasizing Reduction of Students' Academic Burden",
    summary: "The Ministry of Education releases new policies aimed at promoting quality education and reducing mechanical memorization.",
    content: "<p>The Ministry of Education today released the 'Guiding Opinions on Further Deepening Education Reform,' clearly proposing to reduce the excessive academic burden on primary and secondary school students and promote the reform of the education evaluation system.</p><p>The new policy requires schools to reduce the total amount of homework, control the frequency of exams, and increase the proportion of practical courses. At the same time, a diversified student evaluation system will be established, no longer using scores as the sole criterion.</p><p>Education experts say this reform direction conforms to educational laws and is conducive to cultivating students' innovative abilities and practical spirit.</p>",
    imageUrl: "https://picsum.photos/id/7/800/450",
    trustScore: 87,
    trustLevel: "high",
    source: "Ministry of Education Official Website",
    date: "2024-03-09T13:50:00Z",
    category: "Education"
  },
  {
    id: 8,
    title: "New Energy Vehicle Sales Hit Record High, Market Share Exceeds Traditional Fuel Vehicles for First Time",
    summary: "Latest data shows that new energy vehicle sales accounted for 50.8% last month, breaking 50% for the first time in history.",
    content: "<p>Data released by the Automobile Industry Association shows that new energy vehicle sales reached 1.2 million units in February, a year-on-year increase of 45%, with market share reaching 50.8%, surpassing traditional fuel vehicles for the first time in history.</p><p>Analysts believe that this milestone marks the automotive industry's accelerated transition to electrification. Policy support, technological progress, and increased consumer awareness are the main factors driving this change.</p><p>At the same time, the construction of charging infrastructure is also accelerating, with the number of charging piles nationwide expected to reach 20 million by 2025.</p>",
    imageUrl: "https://picsum.photos/id/8/800/450",
    trustScore: 90,
    trustLevel: "high",
    source: "Automobile Industry Association",
    date: "2024-03-08T08:40:00Z",
    category: "Environment"
  }
];

const API_BASE_URL = '/api/news/public';

// 通用请求函数
async function fetchWithTimeout(url, options = {}, timeout = 10000) {
  // 在开发环境直接返回mock数据，不发送实际的API请求
  if (import.meta.env.DEV) {
    console.log('Using mock data directly for:', url);
    return null; // 标记使用mock数据
  }

  const controller = new AbortController();
  const timeoutId = setTimeout(() => controller.abort(), timeout);

  try {
    console.log('API Request:', url);
    const response = await fetch(url, {
      ...options,
      signal: controller.signal
    });
    clearTimeout(timeoutId);

    if (!response.ok) {
      throw new Error(`HTTP error! status: ${response.status}`);
    }

    const data = await response.json();
    console.log('API Response:', url, data);
    return data;
  } catch (error) {
    clearTimeout(timeoutId);
    console.error('API request failed:', error);
    throw error;
  }
}

// 获取所有新闻
export async function getAllNews() {
  let result = await fetchWithTimeout(API_BASE_URL);
  // 如果API调用失败，返回mock数据
  if (!result) {
    console.log('Using mock data for getAllNews');
    return mockNews;
  }
  // 转换后端返回的image字段为前端期望的imageUrl字段
  console.log('Converting backend news data, original:', result);
  const converted = result.map(news => {
    const imageUrl = news.image || `https://picsum.photos/id/${news.id%100}/800/450`;
    console.log(`Converting news ${news.id}: image=${news.image} -> imageUrl=${imageUrl}`);
    return {
      ...news,
      imageUrl
    };
  });
  console.log('Converted news data:', converted);
  return converted;
}

// 根据ID获取新闻
export async function getNewsById(id) {
  let result = await fetchWithTimeout(`${API_BASE_URL}/${id}`);
  // 如果API调用失败或未找到，从mock数据中查找
  if (!result) {
    const news = mockNews.find(item => item.id === Number(id));
    if (!news) {
      throw new Error('News not found');
    }
    return news;
  }
  // 转换后端返回的image字段为前端期望的imageUrl字段
  return {
    ...result,
    imageUrl: result.image || `https://picsum.photos/id/${result.id%100}/800/450`
  };
}

// 根据分类获取新闻
export async function getNewsByCategory(category) {
  let result = await fetchWithTimeout(`${API_BASE_URL}/category/${category}`);
  // 如果API调用失败，从mock数据中过滤
  if (!result) {
    return mockNews.filter(item => item.category.toLowerCase() === category.toLowerCase());
  }
  // 转换后端返回的image字段为前端期望的imageUrl字段
  return result.map(news => ({
    ...news,
    imageUrl: news.image || `https://picsum.photos/id/${news.id%100}/800/450`
  }));
}

// 投票（支持、反对、中立）
export async function voteNews(newsId, voteType) {
  // voteType: 'true', 'false', 'neutral'
  try {
    const result = await fetchWithTimeout(`${API_BASE_URL}/${newsId}/vote`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify({ voteType })
    });
    return result || { success: true, message: 'Vote successful' };
  } catch (error) {
    console.error('Vote failed:', error);
    // 在开发环境返回模拟成功响应
    if (import.meta.env.DEV) {
      console.log('Using mock vote response');
      return { success: true, message: 'Vote successful', mock: true };
    }
    throw error;
  }
}

// 获取新闻投票统计
export async function getNewsVotes(newsId) {
  try {
    const result = await fetchWithTimeout(`${API_BASE_URL}/${newsId}/votes`);
    return result || { true: 0, false: 0, neutral: 0 };
  } catch (error) {
    console.error('Failed to get vote statistics:', error);
    // 在开发环境返回模拟统计数据
    if (import.meta.env.DEV) {
      console.log('Using mock vote statistics data');
      return { true: Math.floor(Math.random() * 100), false: Math.floor(Math.random() * 50), neutral: Math.floor(Math.random() * 30) };
    }
    throw error;
  }
}