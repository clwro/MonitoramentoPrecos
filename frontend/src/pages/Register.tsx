import { useNavigate } from 'react-router-dom';
import AuthForm from '@/components/AuthForm';
import { useToast } from '@/hooks/use-toast';

const Register = () => {
  const navigate = useNavigate();
  const { toast } = useToast();

  const handleSuccess = () => {
    toast({
      title: "Conta criada com sucesso!",
      description: "Agora você pode fazer login.",
    });
    
    navigate('/login');
  };

  return <AuthForm type="register" onSubmitSuccess={handleSuccess} />;
};

export default Register;