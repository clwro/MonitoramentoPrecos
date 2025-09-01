import { useNavigate } from 'react-router-dom';
import AuthForm from '@/components/AuthForm';
import { useToast } from '@/hooks/use-toast';

const Login = () => {
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleSuccess = () => {
      toast({
        title: "Login realizado com sucesso!",
        description: "Bem-vindo de volta.",
      });
      
      navigate('/dashboard');
  };
    return <AuthForm type="login" onSubmitSuccess={handleSuccess} />; 
};

export default Login;