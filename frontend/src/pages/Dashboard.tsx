import { useState, useEffect, useCallback } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Bar, BarChart, CartesianGrid, XAxis, YAxis, ResponsiveContainer } from 'recharts';
import { ChartContainer, ChartConfig, ChartTooltip, ChartTooltipContent, ChartLegend, ChartLegendContent } from '@/components/ui/chart';
import { useToast } from '@/hooks/use-toast';
import Navbar from '@/components/Navbar';
import api from '../api/api';

interface Price {
  data: string;
  region: string;
  price: number;
}

interface ChartData {
  month: string;
  price: number;
}

const chartConfig = {
  preco: {
    label: "Preço",
    color: "#8884d8"
  },
} satisfies ChartConfig

const Dashboard = () => {
  const [chartData, setChartData] = useState<Record<string, ChartData[]>>({});
  const [isLoading, setIsLoading] = useState(true);
  const { toast } = useToast();
  const navigate = useNavigate();

  const fetchPrices = useCallback(async () => {
    try {
      setIsLoading(true);
      const response = await api.get<Price[]>('/precos-mensais');
      const processedData = processDataForCharts(response.data);
      setChartData(processedData);
    } catch (err) {
      toast({
        title: `Não foi possível carregar os preços.`,
        description: 'Tente novamente mais tarde.',
        variant: "destructive"
      });
      console.error("Erro ao buscar os preços: ", err);
    } finally {
      setIsLoading(false);
    }
  }, [toast]);

  useEffect(() => {
    const isAuthenticated = !!localStorage.getItem('token');
    if (!isAuthenticated) {
      toast({
        title: "Usuário não autenticado!",
        description: 'Você precisa estar logado para visualizar esta página.',
        variant: 'destructive',
      });
      navigate('/');
    } else {
      fetchPrices();
    }
  }, [navigate, toast, fetchPrices]);

  const handleCollectPrices = async () => {
    try {
      await api.post('/coletar-precos');
      toast({
        title: 'Coleta de preços iniciada!',
        description: 'Os novos preços estarão disponíveis em breve.',
      });
      fetchPrices();
    } catch (err) {
      toast({
        title: 'Erro ao iniciar a coleta de preços.',
        description: 'Tente novamente mais tarde.',
        variant: 'destructive',
      });
    }
  };

  const processDataForCharts = (data: Price[]): Record<string, ChartData[]> => {
  const regionalData: Record<string, Record<string, number>> = {};

  data.forEach(price => {
    const parts = price.data.split('/');
    const day = parseInt(parts[0], 10);
    const monthIndex = parseInt(parts[1], 10) - 1; 
    const year = parseInt(parts[2], 10);

    const date = new Date(year, monthIndex, day);

    const monthName = date.toLocaleString('pt-BR', { month: 'long' });

    if (!regionalData[price.region]) {
      regionalData[price.region] = {};
    }
    const yearMonthKey = `${monthName.substring(0, 3)}/${date.getFullYear()}`;
    regionalData[price.region][yearMonthKey] = price.price;
  });

  const chartData: Record<string, ChartData[]> = {};

  for (const region in regionalData) {
    chartData[region] = Object.keys(regionalData[region]).map(yearMonthKey => {
      return {
        month: yearMonthKey.split('/')[0],
        price: regionalData[region][yearMonthKey]
      };
    });
  }

  return chartData;
};

  return (
    <div className="min-h-screen bg-background">
      <Navbar />
      <div className="max-w-7xl mx-auto p-6">
        <div className="flex justify-between items-center mb-8">
          <div>
            <h1 className="text-3xl font-bold">Dashboard de Preços</h1>
            <p className="text-muted-foreground">Visualize os preços por região.</p>
          </div>
          <Button onClick={handleCollectPrices}>Coletar Novos Preços</Button>
        </div>

        {isLoading ? (
          <div>Carregando...</div>
        ) : (
          <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
            {Object.keys(chartData).map(region => (
              <Card key={region}>
                <CardHeader>
                  <CardTitle>{region}</CardTitle>
                  <CardDescription>Preços por mês</CardDescription>
                </CardHeader>
                <CardContent>
                  <ChartContainer config= {chartConfig}>
                  <ResponsiveContainer width="100%" height={300}>
                    <BarChart data={chartData[region]}>
                      <CartesianGrid strokeDasharray="3 3" />
                      <XAxis dataKey="month" />
                      <YAxis />
                      <ChartTooltip content={<ChartTooltipContent />} />
                      <ChartLegend content={<ChartLegendContent />} />
                      <Bar dataKey="price" fill="#9993ddff" name="Preço" barSize={30}/>
                    </BarChart>
                  </ResponsiveContainer>
                  </ChartContainer>
                </CardContent>
              </Card>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;