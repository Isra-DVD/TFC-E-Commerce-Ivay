import React from 'react';
import {
    Card,
    CardContent,
    CardMedia,
    Typography,
    Box,
    IconButton,
    Menu,
    MenuItem,
    ListItemIcon,
    ListItemText,
} from '@mui/material';
import MoreVertIcon from '@mui/icons-material/MoreVert';
import EditIcon from '@mui/icons-material/Edit';
import DeleteIcon from '@mui/icons-material/Delete';
import { colors } from '../../constants/styles';

const ProductCard = ({ product, onEdit, onDelete }) => {
    const [anchorEl, setAnchorEl] = React.useState(null);
    const open = Boolean(anchorEl);

    const handleClick = (event) => {
        event.stopPropagation();
        setAnchorEl(event.currentTarget);
    };
    const handleClose = () => {
        setAnchorEl(null);
    };

    const handleEdit = () => {
        if (onEdit) onEdit(product.id);
        handleClose();
    };

    const handleDelete = () => {
        if (onDelete) onDelete(product.id);
        handleClose();
    };

    // Fallback for missing product data
    if (!product || !product.id) {
        return (
            <Card sx={{ p: 2, height: { xs: "auto", sm: 300, md: 250 }, width: { xs: "100%", sm: 100, md: 170 }, display: 'flex', alignItems: 'center', justifyContent: 'center', border: `1px solid ${colors.error?.main || 'red'}` }}>
                <Typography color="error">Error: Producto no válido.</Typography>
            </Card>
        );
    }


    return (
        <Card
            sx={{
                position: 'relative',
                display: 'flex',
                flexDirection: 'column',
                height: { xs: "auto", sm: 300, md: 250 },
                width: { xs: "100%", sm: 100, md: 170 },
                border: `1px solid ${colors.grey[300]}`,
                transition: 'box-shadow 0.3s ease-in-out, transform 0.2s ease-in-out',
                '&:hover': {
                    boxShadow: `0 4px 12px rgba(0,0,0,0.08)`,
                    transform: 'translateY(-2px)'
                },
                overflow: 'hidden'
            }}
        >
            {/* Action Button (MoreVertIcon) - Top Right */}
            <IconButton
                aria-label="actions"
                size="small"
                onClick={handleClick}
                sx={{
                    position: 'absolute',
                    top: 4,
                    right: 4,
                    zIndex: 2,
                    backgroundColor: 'rgba(255,255,255,0.5)',
                    '&:hover': {
                        backgroundColor: 'rgba(255,255,255,0.8)',
                    }
                }}
            >
                <MoreVertIcon fontSize="small" />
            </IconButton>

            <Box
                sx={{
                    width: '100%',
                    paddingTop: '75%',
                    backgroundColor: colors.grey[100],
                    overflow: 'hidden',
                    position: 'relative',
                }}
            >
                <CardMedia
                    component="img"
                    sx={{
                        position: 'absolute',
                        top: 0,
                        left: 0,
                        width: '100%',
                        height: '100%',
                        objectFit: 'contain',
                        p: 1,
                    }}
                    image={product.imageUrl || "/intel-product.jpg"}
                    alt={product.name}
                    onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = "/intel-product.jpg";
                    }}
                />
            </Box>

            <CardContent
                sx={{
                    flexGrow: 1,
                    p: 1.5,
                    pt: 1,
                    '&:last-child': { pb: 1.5 },
                    display: 'flex',
                    flexDirection: 'column',
                    justifyContent: 'space-between',
                    overflow: 'hidden',
                }}
            >
                <Box sx={{ minHeight: '4.5em' }}>
                    <Typography
                        component="div"
                        variant="subtitle1"
                        fontWeight="medium"
                        title={product.name}
                        sx={{
                            mb: 0.5,
                            overflow: 'hidden',
                            textOverflow: 'ellipsis',
                            display: '-webkit-box',
                            WebkitLineClamp: 2,
                            WebkitBoxOrient: 'vertical',
                            lineHeight: 1.4,
                            maxHeight: '2.8em',
                            minHeight: '1.4em',
                        }}
                    >
                        {product.name || "Nombre no disponible"}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" component="div" noWrap>
                        Precio: €{product.price?.toFixed(2) ?? 'N/A'}
                    </Typography>
                    <Typography variant="body2" color="text.secondary" component="div" noWrap>
                        Stock: {product.stock ?? 'N/A'} unidades
                    </Typography>
                </Box>
                <Box sx={{ height: '24px' }} />
            </CardContent>

            <Menu
                anchorEl={anchorEl}
                open={open}
                onClose={handleClose}
            >
                <MenuItem onClick={handleEdit}>
                    <ListItemIcon><EditIcon fontSize="small" /></ListItemIcon>
                    <ListItemText>Editar</ListItemText>
                </MenuItem>
                <MenuItem onClick={handleDelete} sx={{ color: colors.error.main }}>
                    <ListItemIcon><DeleteIcon fontSize="small" sx={{ color: colors.error.main }} /></ListItemIcon>
                    <ListItemText>Eliminar</ListItemText>
                </MenuItem>
            </Menu>
        </Card>
    );
};

export default ProductCard;