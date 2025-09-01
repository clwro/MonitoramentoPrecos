import { Link, useNavigate } from 'react-router-dom';
import { Button } from '@/components/ui/button';
import { ChartNoAxesCombined, User, LogOut } from 'lucide-react';
import { ModeToggle } from './mode-toggle';

const Navbar = () => {
  const navigate = useNavigate();
  const isAuthenticated = !!localStorage.getItem('token');

  const handleLogout = () => {
    localStorage.removeItem('token');
    navigate('/login');
  };

  return (
    <nav className="border-b bg-card shadow-sm">
      <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex justify-between items-center h-16">
          <div className="flex items-center space-x-4">
            <Link to="/dashboard" className="flex items-center space-x-2 text-primary font-bold text-xl">
              <ChartNoAxesCombined className="h-6 w-6" />
              <span>Monitoramento de Preços</span>
            </Link>
            
            <Link to="/">
              <Button variant="ghost">Início</Button>
            </Link>

            {isAuthenticated && (
              <Link to="/dashboard">
                <Button variant="ghost">Dashboard</Button>
              </Link>
            )}
          </div>

          <div className="flex items-center space-x-2">
            <ModeToggle />
            {isAuthenticated ? (
              <Button variant="ghost" onClick={handleLogout} className="text-muted-foreground">
                <LogOut className="h-4 w-4 mr-2" />
                Sair
              </Button>
            ) : (
              <>
                <Link to="/login">
                  <Button variant="ghost">Entrar</Button>
                </Link>
                <Link to="/register">
                  <Button variant="default">Cadastrar</Button>
                </Link>
              </>
            )}
          </div>
        </div>
      </div>
    </nav>
  );
};

export default Navbar;