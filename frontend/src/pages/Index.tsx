import { Link } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { ChartNoAxesCombined, UserPlus, LogIn } from 'lucide-react';

const Index = () => {
  return (
    <div className="min-h-screen" style={{ background: 'var(--gradient-soft)' }}>
      <div className="flex items-center justify-center min-h-screen px-4">
        <div className="text-center max-w-2xl">
          <div className="mb-8">
            <div className="flex justify-center mb-4">
              <ChartNoAxesCombined className="h-16 w-16 text-primary" />
            </div>
            <h1 className="text-4xl font-bold mb-4 text-foreground">Monitoramento de Preços</h1>
            <p className="text-xl text-muted-foreground mb-8">
              Um sistema para monitorar os preços médios do mercado de energia!
            </p>
          </div>
          
          <div className="flex flex-col sm:flex-row gap-4 justify-center">
            <Link to="/register">
              <Button size="lg" className="w-full sm:w-auto">
                <UserPlus className="h-5 w-5 mr-2" />
                Criar Conta
              </Button>
            </Link>
            
            <Link to="/login">
              <Button variant="outline" size="lg" className="w-full sm:w-auto">
                <LogIn className="h-5 w-5 mr-2" />
                Entrar
              </Button>
            </Link>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Index;
